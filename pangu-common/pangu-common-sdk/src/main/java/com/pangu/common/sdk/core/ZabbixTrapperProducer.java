package com.pangu.common.sdk.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.common.zabbix.service.SenderDataService;
import com.pangu.manager.api.RemoteDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultProducer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ZabbixTrapperProducer extends DefaultProducer {

    private final ExecutorService itemValueThread = Executors.newFixedThreadPool(20);

    private final SenderDataService senderDataService;

    private final RemoteDeviceService remoteDeviceService;

    public ZabbixTrapperProducer(Endpoint endpoint, SenderDataService senderDataService, RemoteDeviceService remoteDeviceService) {
        super(endpoint);
        this.senderDataService = senderDataService;
        this.remoteDeviceService = remoteDeviceService;
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();

        if (ObjectUtil.isNull(message.getBody()) ||  !(message.getBody() instanceof List)) {
            return;
        }

        List<DeviceValue> values = message.getBody(List.class);

        for (DeviceValue deviceValue : values) {
            if (StrUtil.isEmpty(deviceValue.getDeviceId()) || CollectionUtil.isEmpty(deviceValue.getAttributes())) {
                log.error("process deviceValue data error，{}", new Gson().toJson(deviceValue));
                continue;
            }
            Long id = remoteDeviceService.getDeviceIdByCode(deviceValue.getDeviceId());
            if (ObjectUtil.isNull(id)){
                log.warn("device id : {} not exist", id);
                continue;
            }
            deviceValue.setDeviceId(id.toString());
            itemValueThread.submit(() -> {
                // 通过线程池开始发送数据
                senderDataService.sendData(deviceValue);
            });
        }

        exchange.getMessage().setBody("{\"success\":\"true\"}");
    }

    @Override
    public void close() throws IOException {
        super.close();
    }


}
