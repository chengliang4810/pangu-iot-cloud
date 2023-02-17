package com.pangu.common.zabbix.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.constant.CommonConstant;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.model.ItemValue;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxValue;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

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

    @Autowired(required = false)
    private ReceiveDataService receiveDataService;

    @Autowired(required = false)
    private ReceiveProblemService receiveProblemService;

    /**
     * 接收zabbix实时数据
     *
     * @param channel 通道
     * @param message 消息
     */
    @RabbitHandler
    @RabbitListener(queues = "#{zabbixInputDataQueue.name}", ackMode= "MANUAL")
    public void receiveMessage(Channel channel, Message message) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        ThreadUtil.execAsync(() -> {
            try {
                if (receiveDataService == null && receiveProblemService == null){
                    //负载到不进行消费的服务，消息重新投递
                    channel.basicReject(deliveryTag, true);
                    return;
                }

                Map<String, Object> result = JsonUtils.parseObject(message.getBody(), Map.class);

                if (ObjectUtil.isNotNull(receiveDataService) && ObjectUtil.isNotNull(result.get("itemid"))){
                    receiveDataService.receiveData(JsonUtils.parseObject(message.getBody(), ZbxValue.class));
                    // 消息确认
                    channel.basicAck(deliveryTag, false);
                } else if (ObjectUtil.isNotNull(receiveProblemService) && ObjectUtil.isNotNull(result.get("eventid"))){
                    receiveProblemService.receiveProblems(JsonUtils.parseObject(message.getBody(), ZbxProblem.class));
                    // 消息确认
                    channel.basicAck(deliveryTag, false);
                    // receiveDataService.receiveProblems(JsonUtils.parseObject(message.getBody(), ZbxProblem.class));
                } else {
                    //负载到不进行消费的服务，消息重新投递
                    log.info("receiveProblemService is {}", receiveProblemService);
                    log.info("receiveDataService is {}", receiveDataService);
                    channel.basicReject(deliveryTag, true);
                    //log.info("未知的zabbix数据类型 {}", result);
                }
                 // 消息确认
                 // channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                try {
                    channel.basicReject(deliveryTag, true);
                } catch (IOException ex) {
                    log.debug("消息确认失败");
                }
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * 通过rabbitmq发送单条数据到Zabbix
     * 不支持指定数据时间
     *
     * @param itemValue zabbix item值
     */
    public void sendMessage(ItemValue itemValue) {
        Assert.notNull(itemValue, "itemValue is null");
        rabbitTemplate.convertAndSend(CommonConstant.Rabbit.TOPIC_EXCHANGE_ZABBIX_OUTPUT, CommonConstant.Rabbit.ROUTING_ZABBIX_OUTPUT_VALUE, JsonUtils.toJsonString(itemValue));
    }

}
