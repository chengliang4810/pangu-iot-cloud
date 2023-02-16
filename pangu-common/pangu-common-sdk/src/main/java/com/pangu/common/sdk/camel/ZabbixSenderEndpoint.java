package com.pangu.common.sdk.camel;

import org.apache.camel.*;
import org.apache.camel.support.DefaultConsumer;
import org.apache.camel.support.DefaultEndpoint;

/**
 * zabbix发送端点
 *
 * @author chengliang4810
 * @date 2023/02/15 16:47
 */
public class ZabbixSenderEndpoint extends DefaultEndpoint {

    private final ZabbixTrapperProducer producer;

    public ZabbixSenderEndpoint() {
        super();
        this.producer = new ZabbixTrapperProducer(this);
    }

    @Override
    public Producer createProducer() throws Exception {
        return producer; // 每次 process 都会创建一个对象, 单例返回
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new DefaultConsumer(this, exchange -> {
            //TODO
            Message in = exchange.getIn();
            String body = in.getBody(String.class);
            System.out.println(body);
        });
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
