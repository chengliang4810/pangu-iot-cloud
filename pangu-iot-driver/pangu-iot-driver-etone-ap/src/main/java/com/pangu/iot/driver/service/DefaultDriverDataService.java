package com.pangu.iot.driver.service;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultDriverDataService extends RouteBuilder {

    @Value("${udp.port}")
    private Integer port;

    @Override
    public void configure() throws Exception {
        from("netty:udp://0.0.0.0:" + port)
                .log("receive data from zabbix: ${body}");
    }


}
