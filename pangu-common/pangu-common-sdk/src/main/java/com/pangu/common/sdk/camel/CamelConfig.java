package com.pangu.common.sdk.camel;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class CamelConfig {

    @Bean("zabbix")
    public ZabbixSenderEndpoint zabbixSenderEndpoint() {
        return new ZabbixSenderEndpoint();
    }

}
