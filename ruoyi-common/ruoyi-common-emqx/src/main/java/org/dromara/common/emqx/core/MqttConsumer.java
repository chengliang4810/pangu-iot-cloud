package org.dromara.common.emqx.core;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.decoder.MessageDecoder;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.common.MqttMessage;

/**
 * mqtt消费者
 *
 * @author chengliang
 * @date 2022/08/01
 */
@Slf4j
public abstract class MqttConsumer<T> implements IMqttMessageListener, MessageDecoder<T> {

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        T decoder = decoder(mqttMessage);
        messageHandler(topic, decoder);
    }

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param entity 实体
     */
    protected abstract void messageHandler(String topic, T entity);

}
