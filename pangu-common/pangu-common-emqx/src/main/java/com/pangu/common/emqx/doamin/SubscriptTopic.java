package com.pangu.common.emqx.doamin;

import com.pangu.common.emqx.constant.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;

/**
 * 订阅主题
 *
 * @author chengliang
 * @date 2022/08/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptTopic {

    /**
     * 原主题
     */
    private String topic;
    /**
     * 订阅主题
     */
    private String subTopic;
    /**
     * 订阅模式
     */
    private Pattern pattern;
    /**
     * 消息等级
     */
    private int qos;
    /**
     * 消费类
     */
    private IMqttMessageListener messageListener;

}
