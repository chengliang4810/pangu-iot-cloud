package com.pangu.common.sdk.config;

import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.doamin.SubscriptTopic;
import com.pangu.common.sdk.mqtt.FunctionExecConsumer;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

/**
 * 默认主题配置
 *
 * @author chengliang
 * @date 2023/02/20
 */
@Configuration
public class DefaultTopicConfig {

    @Resource
    private List<SubscriptTopic> subscriptTopics;

    @Resource
    private FunctionExecConsumer functionExecConsumer;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 驱动topic
     *
     * @return {@link SubscriptTopic}
     */
    @Bean
    public SubscriptTopic driverGroupTopic() {
        SubscriptTopic subscriptTopic = new SubscriptTopic("iot/device/#/function/#/exec", "$share/" + applicationName + "Group/iot/device/#/function/#/exec", Pattern.SHARE, 2, (IMqttMessageListener) functionExecConsumer);
        subscriptTopics.add(subscriptTopic);
        return subscriptTopic;
    }

}
