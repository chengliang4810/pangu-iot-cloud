package com.pangu.common.emqx.core;

import com.pangu.common.emqx.decoder.MessageDecoder;
import lombok.extern.slf4j.Slf4j;
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
        try {
            T decoder = decoder(mqttMessage);
            messageHandler(topic, decoder);
        } catch (Exception ex) {
            //解决业务处理错误导致断线问题
            log.error(ex.getMessage());
        }
    }

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param entity 实体
     */
    protected abstract void messageHandler(String topic, T entity);

}
