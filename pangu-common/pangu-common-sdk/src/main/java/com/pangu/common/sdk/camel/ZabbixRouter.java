package com.pangu.common.sdk.camel;

import org.apache.camel.builder.RouteBuilder;

public class ZabbixRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
            from("timer://myTimer?period=2s")
                .setBody().simple("Current time is ${header.firedTime}")
                .to("zabbix");
    }

}
