package org.dromara.common.emqx.doamin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.emqx.constant.Pattern;
import org.eclipse.paho.mqttv5.client.IMqttMessageListener;

/**
 * 订阅主题
 *
 * @author chengliang
 * @date 2023/06/21
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
