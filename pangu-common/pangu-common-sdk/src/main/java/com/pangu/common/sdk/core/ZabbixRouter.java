package com.pangu.common.sdk.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.core.utils.ip.AddressUtils;
import com.pangu.common.emqx.properties.EmqProperties;
import com.pangu.common.sdk.bean.DriverProperty;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.manager.api.domain.dto.DriverStatus;
import com.pangu.manager.api.enums.OnlineStatus;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZabbixRouter extends RouteBuilder {


    @Autowired(required = false)
    private DriverDataService driverDataService;
    @Resource
    private DriverContext driverContext;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private Integer port;

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private EmqProperties emqProperties;

    @Override
    public void configure() throws Exception {
        // 组织唯一驱动标识
        String emqClientId = applicationName + "_" + AddressUtils.localHost() + ":" + port + "_CAMEL";
        String primaryKey = driverProperty.getName() + "_" + AddressUtils.localHost() + "_" + port;
        String mqttUri = "paho-mqtt5:" + StrUtil.format(IotConstants.Topic.Driver.DRIVER_TOPIC_HEARTBEAT_URI_TPL, applicationName, emqProperties.getBroker(), emqClientId, emqProperties.getPassword(), emqProperties.getUserName());

        // 发送驱动心跳
        from("timer://DriverHeartbeatTimer?period=" + driverProperty.getSchedule().getHeartbeat() + "&delay=5s")
            .process(exchange -> {
                exchange.getMessage().setBody(JsonUtils.toJsonString(new DriverStatus(primaryKey, OnlineStatus.ONLINE)));
            })
            .to(mqttUri);

        // 如果未提供数据服务, 则不读取设备数据
        if (null == driverDataService) {
            log.warn("未提供数据服务, 不读取设备数据");
            return;
        }

        driverDataService.setDriverContext(driverContext);
        // 读取设备数据
        from("timer://ReadDeviceValueTimer?period=" + driverProperty.getSchedule().getRead())
            .process(exchange -> {
                // 子设备集合
                Map<Long, Device> deviceMap = driverContext.getDriverMetadata().getDeviceMap();
                List<DeviceValue> deviceValues = new ArrayList<>();

                // 读取设备数据,
                for (Device device : deviceMap.values()) {
                    List<DeviceAttribute> deviceAttributes = new ArrayList<>(driverContext.getDriverMetadata().getProfileAttributeMap().get(device.getId()).values());
                    try {
                        // 捕获异常, 防止一个设备读取失败导致其他设备数据无法读取
                        DeviceValue deviceValue = driverDataService.read(device, deviceAttributes);
                        if (ObjectUtil.isNotNull(deviceValue) && CollUtil.isNotEmpty(deviceValue.getAttributes())) {
                            deviceValues.add(deviceValue);
                        }
                    } catch (Exception e) {
                        log.error("读取设备数据异常: {}", e.getMessage());
                    }
                }
                // 发送设备数据
                exchange.getIn().setBody(deviceValues);
            })
            .filter(exchange -> CollectionUtil.isNotEmpty(exchange.getMessage().getBody(List.class)))
            .to("zabbix");

    }

}
