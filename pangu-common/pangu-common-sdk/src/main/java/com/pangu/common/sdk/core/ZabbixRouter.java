package com.pangu.common.sdk.core;

import com.pangu.common.sdk.service.DriverDataService;
import org.apache.camel.builder.RouteBuilder;

import javax.annotation.Resource;

public class ZabbixRouter extends RouteBuilder {


    @Resource
    private DriverDataService driverDataService;

    @Override
    public void configure() throws Exception {
        // 发送服务心跳
        from("timer://TestTimer?period=5s")
            .process(exchange -> {
                exchange.getMessage().setBody("driver");
            })
            .to("rabbitmq:pangu.exchange.driver.heartbeat?queue=queue.driver.heartbeat&routingKey=driver_route_heartbeat");

        // 读取设备数据
        from("timer://TestTimer2?period=2000s")
            .process(exchange -> exchange.getIn().setBody(driverDataService.read()))
            .to("zabbix");

        // 控制设备
    }

}
