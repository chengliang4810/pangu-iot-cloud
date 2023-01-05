package com.pangu.common.zabbix.service;

import com.pangu.common.core.constant.CommonConstant;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.model.ItemValue;
import com.pangu.common.zabbix.model.ItemValueResult;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * zabbix数据服务 通过消息队列发送数据到zabbix
 *
 * @author chengliang
 * @date 2022/11/07
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ZbxDataService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 接收zabbix实时数据
     *
     * @param channel 通道
     * @param message 消息
     */
    @RabbitHandler
    @RabbitListener(queues = "#{zabbixInputDataQueue.name}")
    public void receiveMessage(Channel channel, Message message) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            ItemValueResult itemValue = JsonUtils.parseObject(message.getBody(), ItemValueResult.class);
            log.debug("zabbix data: {}", itemValue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 通过rabbitmq发送消息数据到Zabbix
     *
     * @param itemValue zabbix item值
     */
    public void sendMessage(ItemValue itemValue) {
        rabbitTemplate.convertAndSend(CommonConstant.Rabbit.TOPIC_EXCHANGE_ZABBIX_OUTPUT, CommonConstant.Rabbit.ROUTING_ZABBIX_OUTPUT_VALUE, itemValue);
    }

}
