package com.pangu.common.zabbix.config;

import com.pangu.common.core.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.pangu.common.core.constant.CommonConstant.Rabbit.*;

/**
 * @author chengliang
 */
@Slf4j
@Configuration
public class TopicRabbitConfig {

    @Bean
    TopicExchange zabbixInputDataExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_ZABBIX_INPUT, true, false);
    }

    @Bean
    Queue zabbixInputDataQueue() {
        Map<String, Object> arguments = new HashMap<>();
        // 1天：24 * 60 * 60 * 1000 = 86400000
        arguments.put(CommonConstant.Rabbit.MESSAGE_TTL, 86400000);
        return new Queue(QUEUE_ZABBIX_INPUT_VALUE, true, false, false, arguments);
    }

    @Bean
    Binding zabbixDataBinding(TopicExchange zabbixInputDataExchange, Queue zabbixInputDataQueue) {
        return BindingBuilder
                .bind(zabbixInputDataQueue)
                .to(zabbixInputDataExchange)
                .with(ROUTING_ZABBIX_INPUT_VALUE);
    }

    @Bean
    TopicExchange zabbixOutputDataExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_ZABBIX_OUTPUT, true, false);
    }

    @Bean
    Queue zabbixOutputDataQueue() {
        Map<String, Object> arguments = new HashMap<>();
        // 1天： 24 * 60 * 60 * 1000 = 86400000
        arguments.put(CommonConstant.Rabbit.MESSAGE_TTL, 86400000);
        return new Queue(QUEUE_ZABBIX_OUTPUT_VALUE, true, false, false, arguments);
    }

    @Bean
    Binding zabbixOutputDataBinding(TopicExchange zabbixOutputDataExchange, Queue zabbixOutputDataQueue) {
        return BindingBuilder
                .bind(zabbixOutputDataQueue)
                .to(zabbixOutputDataExchange)
                .with(ROUTING_ZABBIX_OUTPUT_VALUE);
    }


}
