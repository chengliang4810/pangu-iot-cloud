package com.pangu.common.sdk.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {


    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                // your custom configuration goes here
                context.addComponent("zabbix", new ZabbixDefaultComment());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }



}
