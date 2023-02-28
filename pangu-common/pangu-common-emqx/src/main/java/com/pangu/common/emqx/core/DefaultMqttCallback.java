package com.pangu.common.emqx.core;

import com.pangu.common.core.utils.SpringUtils;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.doamin.SubscriptTopic;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.MqttSubscription;
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

    private final List<SubscriptTopic> topicMap;

    /**
     * 默认mqtt回调
     *
     * @param topicMap 订阅列表
     */
    public DefaultMqttCallback(List<SubscriptTopic> topicMap) {
        this.topicMap = topicMap;
    }


    /**
     * 断开连接
     *
     */
    @Override
    @SneakyThrows
    public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {
        MqttClient client = SpringUtils.getBean(MqttClient.class);
        MqttConnectionOptions option = SpringUtils.getBean(MqttConnectionOptions.class);
//        while (!client.isConnected()) {
//            log.info("client is not connected, try to reconnect");
//            client.connect(option);
//            ThreadUtil.sleep(10000);
//        }
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
        return MqttTopicValidator.isMatched(topicFilter, topic);
    }


    /**
     * 连接emq服务器后触发
     */
    @SneakyThrows
    @Override
    public void connectComplete(boolean b, String s) {
        MqttClient client = SpringUtils.getBean(MqttClient.class);
        if (client.isConnected()) {
            MqttSubscription[] mqttSubscriptions = new MqttSubscription[topicMap.size()];
            IMqttMessageListener[] messageListeners = new IMqttMessageListener[topicMap.size()];
            for (int i = 0; i < topicMap.size(); i++) {
                mqttSubscriptions[i] = new MqttSubscription(topicMap.get(i).getSubTopic(), topicMap.get(i).getQos());
                messageListeners[i] = topicMap.get(i).getMessageListener();
                log.info("订阅主题 {}", topicMap.get(i).getSubTopic());
            }
            client.subscribe(mqttSubscriptions, messageListeners);
            log.info("共订阅 {}   个主题!", topicMap.size());
        }
    }


}
