package com.pangu.common.sdk.core;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.zabbix.service.SenderDataService;
import com.pangu.manager.api.RemoteDeviceService;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;

/**
 * 发送zabbix数据端点
 *
 * @author chengliang4810
 * @date 2023/02/15 16:47
 */
public class ZabbixSenderEndpoint extends DefaultEndpoint {

    private final ZabbixTrapperProducer producer;

    public ZabbixSenderEndpoint(SenderDataService senderDataService, RemoteDeviceService remoteDeviceService) {
        super();
        this.producer = new ZabbixTrapperProducer(this, senderDataService, remoteDeviceService);
    }

    @Override
    public Producer createProducer() throws Exception {
        // 每次 process 都会创建一个对象, 单例返回
        return producer;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        throw new ServiceException("zabbix sender endpoint not support consumer");
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}
