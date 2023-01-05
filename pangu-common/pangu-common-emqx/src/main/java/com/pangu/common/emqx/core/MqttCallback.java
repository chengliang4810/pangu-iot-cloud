package com.pangu.common.emqx.core;

import cn.hutool.core.thread.ThreadUtil;
import com.pangu.common.core.utils.SpringUtils;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.doamin.SubscriptTopic;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

import java.util.List;

/**
 * mqtt回调
 *
 * @author chengliang
 * @date 2022/08/01
 */
@Slf4j
public class MqttCallback implements MqttCallbackExtended {

    private final List<SubscriptTopic> topicMap;

    public MqttCallback(List<SubscriptTopic> topicMap) {
        this.topicMap = topicMap;
    }

    /**
     * 客户端断开后触发
     *
     * @param throwable 异常
     */
    @SneakyThrows
    @Override
    public void connectionLost(Throwable throwable) {
        MqttClient client = SpringUtils.getBean(MqttClient.class);
        MqttConnectOptions option = SpringUtils.getBean(MqttConnectOptions.class);
        while (!client.isConnected()) {
            log.info("client is not connected, try to reconnect");
            client.connect(option);
            ThreadUtil.sleep(1000);
        }
    }

    /**
     * 客户端收到消息触发
     *
     * @param topic   主题
     * @param message 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("topic: {}, message: {}", topic, message);
        for (SubscriptTopic subscriptTopic : topicMap) {
            if (subscriptTopic.getPattern() != Pattern.NONE && isMatched(subscriptTopic.getTopic(), topic)) {
                subscriptTopic.getMessageListener().messageArrived(topic, message);
                return;
            }
        }
    }

    /**
     * 检测一个主题是否为一个通配符表示的子主题
     *
     * @param topicFilter 通配符主题
     * @param topic       子主题
     * @return 是否为通配符主题的子主题
     */
    private boolean isMatched(String topicFilter, String topic) {
        return MqttTopic.isMatched(topicFilter, topic);
    }

    /**
     * 发布消息成功
     *
     * @param token token
     */
    @SneakyThrows
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        String[] topics = token.getTopics();
        for (String topic : topics) {
            log.debug("向主题 {} 发送数据", topic);
        }
    }

    /**
     * 连接emq服务器后触发
     *
     * @param b
     * @param s
     */
    @SneakyThrows
    @Override
    public void connectComplete(boolean b, String s) {
        MqttClient client = SpringUtils.getBean(MqttClient.class);
        if (client.isConnected()) {
            for (SubscriptTopic sub : topicMap) {
                client.subscribe(sub.getSubTopic(), sub.getQos(), sub.getMessageListener());
                log.info("订阅主题 {}", sub.getSubTopic());
            }
            log.info("共订阅 {}   个主题!", topicMap.size());
        }
    }


}
