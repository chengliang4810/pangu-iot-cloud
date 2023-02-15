package com.pangu.common.sdk.camel;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.pangu.common.zabbix.model.ItemValue;
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

    public ZabbixTrapperProducer(Endpoint endpoint) {
        super(endpoint);
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();

        if (message.getBody() == null && !(message.getBody() instanceof List)) {
            return;
        }

        List<ItemValue> values = (List<ItemValue>) message.getBody();

        for (ItemValue itemValue : values) {
            if (StrUtil.isEmpty(itemValue.getHostname()) || StrUtil.isEmpty(itemValue.getItemKey()) || StrUtil.isEmpty(itemValue.getItemValue())) {
                log.error(" process item data error，{}", new Gson().toJson(itemValue));
                continue;
            }
            itemValueThread.submit(() -> {
                // itemDataTransferWorker.in(itemValue);
            });
        }

        exchange.getMessage().setBody("{\"success\":\"true\"}");
    }

    @Override
    public void close() throws IOException {
        super.close();
    }


}
