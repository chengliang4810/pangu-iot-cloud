package com.pangu.common.zabbix.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.constant.CommonConstant;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.domain.DataMessage;
import com.pangu.common.zabbix.domain.ProblemMessage;
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
        ThreadUtil.execAsync(() -> {
            if (receiveDataService == null && receiveProblemService == null){
                //负载到不进行消费的服务，消息重新投递
                asyncRedelivery(channel, message);
                log.warn("没有接收数据或问题的服务");
                return;
            }
            Map<String, Object> result = JsonUtils.parseObject(message.getBody(), Map.class);

            if (ObjectUtil.isNotNull(receiveDataService) && ObjectUtil.isNotNull(result.get("itemid"))){
                // 实时数据
                ZbxValue zbxValue = JsonUtils.parseObject(message.getBody(), ZbxValue.class);
                receiveDataService.receiveData(new DataMessage(channel, message, zbxValue));
            } else if (ObjectUtil.isNotNull(receiveProblemService) && ObjectUtil.isNotNull(result.get("eventid"))){
                // 问题数据
                ZbxProblem zbxProblem = JsonUtils.parseObject(message.getBody(), ZbxProblem.class);
                receiveProblemService.receiveProblems(new ProblemMessage(channel, message, zbxProblem));
            } else {
                //负载到不进行消费的服务，消息重新投递
                asyncRedelivery(channel, message);
            }
        });
    }


    /**
     * 异步返还
     *
     * @param channel 通道
     * @param message 消息
     */
    private void asyncRedelivery(Channel channel, Message message) {
        ThreadUtil.execAsync(() -> {
            try {
                ThreadUtil.sleep(1000);
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
            } catch (IOException e) {
                log.debug("消息确认失败");
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
