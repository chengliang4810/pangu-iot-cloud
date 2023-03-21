package com.pangu.common.sdk.config;

import com.pangu.common.sdk.core.ZabbixRouter;
import com.pangu.common.sdk.core.ZabbixSenderEndpoint;
import com.pangu.common.zabbix.service.SenderDataService;
import com.pangu.manager.api.RemoteDeviceService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Configuration
@RequiredArgsConstructor
public class CamelConfig {

    @Resource
    private final SenderDataService senderDataService;
    @DubboReference
    private final RemoteDeviceService remoteDeviceService;


    @Bean("zabbix")
    public ZabbixSenderEndpoint zabbixSenderEndpoint() {
        return new ZabbixSenderEndpoint(senderDataService, remoteDeviceService);
    }

    @Bean
    public ZabbixRouter zabbixRouter() {
        return new ZabbixRouter();
    }

}
