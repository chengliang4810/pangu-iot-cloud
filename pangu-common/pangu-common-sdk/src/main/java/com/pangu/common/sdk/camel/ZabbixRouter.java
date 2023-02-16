package com.pangu.common.sdk.camel;

import com.pangu.common.zabbix.model.DeviceValue;
import org.apache.camel.builder.RouteBuilder;

import java.util.List;

public abstract class ZabbixRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        // 发送服务心跳
        from("timer://TestTimer?period=5s")
            .process(exchange -> {
                exchange.getMessage().setBody("driver");
            })
            .to("rabbitmq:pangu.exchange.driver.heartbeat?queue=queue.driver.heartbeat?routingKey=driver_route_heartbeat");

        // 读取设备数据
        from("timer://TestTimer2?period=2s")
            .process(exchange -> exchange.getIn().setBody(read()))
            .to("zabbix");

        // 控制设备
    }

    /**
     * 读取设备数据进行上传
     *
     * @return {@link List}<{@link DeviceValue}>
     */
    public abstract List<DeviceValue> read();

}
