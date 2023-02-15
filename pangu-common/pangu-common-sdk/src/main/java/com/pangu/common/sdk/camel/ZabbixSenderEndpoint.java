package com.pangu.common.sdk.camel;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;

/**
 * zabbix发送端点
 *
 * @author chengliang4810
 * @date 2023/02/15 16:47
 */
public class ZabbixSenderEndpoint extends DefaultEndpoint {

    private final ZabbixTrapperProducer producer;

    public ZabbixSenderEndpoint(String endpointUri, ZabbixDefaultComment component) {
        super(endpointUri, component);
        this.producer = new ZabbixTrapperProducer(this);
    }

    @Override
    public Producer createProducer() throws Exception {
        return producer; // 每次 process 都会创建一个对象, 单例返回
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return null; //TODO
    }

    @Override
    public boolean isSingleton() {
        return false;
    }


}
