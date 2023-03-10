package com.pangu.common.emqx.doamin;

import cn.hutool.core.util.IdUtil;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;

public class Test {


    public static void main(String[] args) throws MqttException {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setUserName("admin");
        options.setPassword("admin".getBytes());

        MqttAsyncClient client = new MqttAsyncClient("tcp://10.0.0.34:1883", IdUtil.nanoId(), new MemoryPersistence());
        IMqttToken connect = client.connect(options);
        connect.waitForCompletion();
        System.out.println("connect success");
        IMqttToken subscribe = client.subscribe("$share/pangu-iot-test-driverGroup/iot/device/#/function/#/exec", 2);
        subscribe.waitForCompletion();
        System.out.println("subscribe success");
        System.out.println(subscribe.getResponse());
    }

}
