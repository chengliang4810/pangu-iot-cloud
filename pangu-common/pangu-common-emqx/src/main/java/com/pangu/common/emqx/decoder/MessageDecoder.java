package com.pangu.common.emqx.decoder;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 消息解码器
 *
 * @author chengliang
 * @date 2022/08/01
 */
public interface MessageDecoder<T> {

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    T decoder(MqttMessage message);

}
