package com.pangu.common.sdk.camel;

import com.pangu.common.zabbix.model.ItemValue;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

public abstract class ZabbixRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // 发送服务心跳
        from("timer://TestTimer?period=2s")
            .process(exchange -> {
                exchange.getMessage().setBody(getDeviceValue());
            })
            .to("zabbix");

        // 读取设备数据
        from("timer://TestTimer2?period=2s")
            .process(exchange -> exchange.getIn().setBody("hellohahhaha"))
            .to("zabbix");

        // 控制设备
    }

    public abstract List<ItemValue> getDeviceValue();

}
