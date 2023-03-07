package com.pangu.common.sdk.core;

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
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZabbixRouter extends RouteBuilder {


    @Resource
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
        String emqClientId = applicationName + ":" + AddressUtils.localHost() + ":" + port;
        String primaryKey = driverProperty.getName() + "_" + AddressUtils.localHost() + "_" + port;
        String mqttUri = "paho-mqtt5:" + StrUtil.format(IotConstants.Topic.Driver.DRIVER_TOPIC_HEARTBEAT_URI_TPL, applicationName, emqProperties.getBroker(), emqClientId, emqProperties.getPassword(), emqProperties.getUserName());

        driverDataService.setDriverContext(driverContext);

        // 发送驱动心跳
        from("timer://DriverHeartbeatTimer?period=" + driverProperty.getSchedule().getHeartbeat() + "&delay=5s")
            .process(exchange -> {
                exchange.getMessage().setBody(JsonUtils.toJsonString(new DriverStatus(primaryKey, OnlineStatus.ONLINE)));
            })
            .to(mqttUri);
        //.to("rabbitmq:pangu.exchange.driver.heartbeat?queue=queue.driver.heartbeat&routingKey=driver_route_heartbeat");

        System.out.println("read: " +  driverProperty.getSchedule().getRead());
        // 读取设备数据
        from("timer://TestTimer2?period=" + driverProperty.getSchedule().getRead())
            .process(exchange -> {
                Map<Long, Device> deviceMap = driverContext.getDriverMetadata().getDeviceMap();

                System.out.println("333333" + deviceMap);
                deviceMap.values().parallelStream().forEach(device -> {
                    List<DeviceAttribute> deviceAttributes = new ArrayList<>(driverContext.getDriverMetadata().getProfileAttributeMap().get(device.getId()).values());
                    DeviceValue deviceValue = driverDataService.read(device, deviceAttributes);
                    System.out.println("11111"  + deviceValue);
                    // 读取设备属性值
                    // List<DeviceValue> deviceValues = driverDataService.batchRead(device, deviceAttributes, driverInfo, pointInfo);
                });
                // exchange.getIn().setBody();
            })
            .to("zabbix");

    }

}
