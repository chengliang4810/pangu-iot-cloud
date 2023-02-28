package com.pangu.common.sdk.core;

import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.core.utils.ip.AddressUtils;
import com.pangu.common.emqx.properties.EmqProperties;
import com.pangu.common.sdk.bean.DriverProperty;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.manager.api.domain.dto.DriverStatus;
import com.pangu.manager.api.enums.OnlineStatus;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;

public class ZabbixRouter extends RouteBuilder {


    @Resource
    private DriverDataService driverDataService;

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
        String primaryServiceName = applicationName + ":" + AddressUtils.localHost() + ":" + port;

        String mqttUri = "paho-mqtt5:" + StrUtil.format(IotConstants.Topic.Driver.DRIVER_TOPIC_HEARTBEAT_URI_TPL, applicationName, emqProperties.getBroker(), primaryServiceName, emqProperties.getPassword(), emqProperties.getUserName());

        // 发送驱动心跳
        from("timer://DriverHeartbeatTimer?period="+driverProperty.getSchedule().getHeartbeat()+"&delay=5s")
                .process(exchange -> {
                    exchange.getMessage().setBody(JsonUtils.toJsonString(new DriverStatus(primaryServiceName, OnlineStatus.ONLINE)));
                })
                .to(mqttUri);
        //.to("rabbitmq:pangu.exchange.driver.heartbeat?queue=queue.driver.heartbeat&routingKey=driver_route_heartbeat");

        // 读取设备数据
        from("timer://TestTimer2?period=" + driverProperty.getSchedule().getRead())
                .process(exchange -> exchange.getIn().setBody(driverDataService.read()))
                .to("zabbix");

    }

}
