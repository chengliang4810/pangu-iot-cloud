package com.pangu.common.emqx.doamin;

import com.pangu.common.core.exception.ServiceException;
import lombok.Getter;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.common.MqttException;

import java.io.Serializable;

/**
 * emqx客户端
 * 对mqtt客户端进行简单的封装
 * @author chengliang
 * @date 2023/03/09
 */
public class EmqxClient implements Serializable {

    @Getter
    private final MqttAsyncClient client;

    public void publish(String topic, String payload) {
        this.publish(topic, payload, 0);
    }

    public void publish(String topic, String payload, int qos) {
        this.publish(topic, payload.getBytes(), qos);
    }

    public void publish(String topic, byte[] payload) {
        this.publish(topic, payload, 0);
    }

    public void publish(String topic, byte[] payload, int qos) {
        this.publish(topic, payload, qos, false);
    }

    public void publish(String topic, byte[] payload, int qos, boolean retained) {
        try {
            client.publish(topic, payload, qos, retained);
        } catch (MqttException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public EmqxClient(MqttAsyncClient client) {
        this.client = client;
    }

}
