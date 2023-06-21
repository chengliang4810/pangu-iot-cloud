package org.dromara.common.emqx.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.constant.Pattern;
import org.dromara.common.emqx.doamin.SubscriptTopic;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.eclipse.paho.mqttv5.common.util.MqttTopicValidator;

import java.util.List;

/**
 * mqtt回调
 *
 * @author chengliang
 * @date 2022/08/01
 */
@Slf4j
public class DefaultMqttCallback implements MqttCallback {

    private final List<SubscriptTopic> topicList;
    private final MqttAsyncClient client;

    /**
     * 默认mqtt回调
     *
     * @param client
     * @param topicList 订阅列表
     */
    public DefaultMqttCallback(MqttAsyncClient client, List<SubscriptTopic> topicList) {
        this.client = client;
        this.topicList = topicList;
    }

    /**
     * 断开连接
     *
     */
    @Override
    @SneakyThrows
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        log.debug("mqtt disconnected {}", mqttDisconnectResponse);
    }

    /**
     * mqtt发生错误
     *
     */
    @Override
    public void mqttErrorOccurred(MqttException e) {
        e.printStackTrace();
    }

    /**
     * 发布消息成功
     *
     * @param token mqtt令牌
     */
    @Override
    public void deliveryComplete(IMqttToken token) {
        String[] topics = token.getTopics();
        for (String topic : topics) {
            log.debug("向主题 {} 发送数据", topic);
        }
    }

    /**
     * 身份验证数据包到达
     *
     * @param reasonCode The Reason code, can be Success (0),
     *                   Continue authentication (24)
     *                   Re-authenticate (25).
     * @param mqttProperties mqtt属性
     */
    @Override
    public void authPacketArrived(int reasonCode, MqttProperties mqttProperties) {

    }

    /**
     * 客户端收到消息触发
     *
     * @param topic   主题
     * @param message 消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.debug("topic: {}", topic);
        for (SubscriptTopic subscriptTopic : topicList) {
            // 普通订阅
            if (subscriptTopic.getPattern() == Pattern.NONE && subscriptTopic.getTopic().equals(topic)) {
                subscriptTopic.getMessageListener().messageArrived(topic, message);
                return;
            }
            // 共享订阅
            if (subscriptTopic.getPattern() != Pattern.NONE && isMatched(subscriptTopic.getTopic(), topic)) {
                subscriptTopic.getMessageListener().messageArrived(topic, message);
                return;
            }
        }
        log.debug("未找到匹配的主题");
    }

    /**
     * 检测一个主题是否为一个通配符表示的子主题
     *
     * @param topicFilter 通配符主题
     * @param topic       子主题
     * @return 是否为通配符主题的子主题
     */
    private boolean isMatched(String topicFilter, String topic) {
        return MqttTopicValidator.isMatched(topicFilter, topic);
    }


    /**
     * 连接emq服务器后触发
     */
    @Override
    @SneakyThrows
    public void connectComplete(boolean reconnect, String serverURI) {
        log.debug("mqtt connect complete, reconnect: {}, serverURI: {} isConnected: {}", reconnect, serverURI, client.isConnected());
        if (client.isConnected()) {
            for (SubscriptTopic subscriptTopic : topicList) {
                try {
                    IMqttToken subscribeToken = client.subscribe(subscriptTopic.getSubTopic(), subscriptTopic.getQos());
                    subscribeToken.waitForCompletion(5000);
                    log.info("主题订阅成功 {}", subscriptTopic.getSubTopic());
                } catch (Exception e) {
                    log.error("主题订阅失败 {} : message: {}", subscriptTopic.getSubTopic(), e.getMessage());
                }
            }
        }
    }

}
