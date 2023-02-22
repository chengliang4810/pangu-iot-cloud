package com.pangu.common.emqx.config;

import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.DefaultMqttCallback;
import com.pangu.common.emqx.doamin.SubscriptTopic;
import com.pangu.common.emqx.properties.EmqProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * emqx配置
 *
 * @author chengliang
 * @date 2023/02/21
 */
@Data
@Slf4j
@Component
public class EmqxConfig {

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * MQTT的连接设置
     * @return
     */
    @Bean
    public MqttConnectionOptions getOption(EmqProperties emqProperties) {
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setUserName(emqProperties.getUserName());
        options.setPassword(emqProperties.getPassword().getBytes());
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setCleanStart(emqProperties.getCleanSession());
        //断线重连
        // options.setMaxInflight(1000);
        options.setAutomaticReconnect(emqProperties.getReconnect());
        options.setMaxReconnectDelay(1000);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(emqProperties.getTimeout());
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*10秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(emqProperties.getKeepAlive());
        return options;
    }

    @Bean
    public MqttClient getClient(MqttConnectionOptions options, ApplicationContext applicationContext, EmqProperties emqProperties) throws Exception {
        // 构建Client 设置客户端ID
        MqttClient client = new MqttClient(emqProperties.getBroker(), applicationName + "_" + Inet4Address.getLocalHost().getHostAddress() + ":" + port);

        //得到所有使用@Topic注解的类
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Topic.class);
        List<SubscriptTopic> topicMap = new ArrayList<SubscriptTopic>(beansWithAnnotation.size());
        //遍历所有使用@Topic注解的类
        for (String className : beansWithAnnotation.keySet()) {
            Class<?> classByteCode = beansWithAnnotation.get(className).getClass();
            //获取类的注解属性
            Topic annotation = AnnotationUtils.findAnnotation(classByteCode, Topic.class);
            String topic = annotation.topic();
            int qos = annotation.qos();
            Pattern patten = annotation.patten();
            String group = annotation.group();

            // 判断共享订阅类型
            String subTopic = topic;
            if (patten == Pattern.SHARE) {
                group = group.replace("${spring.application.name}", applicationName);
                subTopic = "$share/" + group + "/" + topic;
            } else if (patten == Pattern.QUEUE) {
                subTopic = "$queue/" + topic;
            }
            topicMap.add(new SubscriptTopic(topic, subTopic, patten, qos, (IMqttMessageListener) applicationContext.getBean(classByteCode)));
        }

        // 添加回调监听
        client.setCallback(new DefaultMqttCallback(topicMap));

        // 链接服务器并获取连接的Token
        IMqttToken iMqttToken = client.connectWithResult(options);
        boolean complete = iMqttToken.isComplete();
        log.info("mqtt建立连接：{}", complete);
        return client;
    }

}
