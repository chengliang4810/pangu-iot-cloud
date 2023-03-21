package com.pangu.common.zabbix.domain;

import cn.hutool.core.thread.ThreadUtil;
import com.pangu.common.core.exception.ServiceException;
import com.rabbitmq.client.Channel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.Message;

import java.io.IOException;
import java.io.Serializable;

public abstract class ZabbixMessage<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 消息队列通道
     */
    protected Channel channel;

    /**
     * 消息队列消息
     */
    protected Message message;

    /**
     * 消息内容
     */
    @Getter
    @Setter
    protected T data;


    public ZabbixMessage(Channel channel, Message message) {
        this.channel = channel;
        this.message = message;
    }

    /**
     * 返还消息
     */
    public void redelivery() {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            channel.basicReject(deliveryTag, true);
        } catch (IOException e) {
            throw new ServiceException("消息返还失败");
        }
    }

    /**
     * 异步返还消息
     */
    public void asyncRedelivery() {
        ThreadUtil.execAsync(this::redelivery);
    }

    /**
     * 异步返还消息, 延迟返还。 防止发生死循环
     */
    public void asyncRedelivery(Long time) {
        ThreadUtil.execAsync(() -> {
            ThreadUtil.sleep(time);
            this.redelivery();
        });
    }

    /**
     * 确认消息
     */
    public void ack() {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            throw new ServiceException("消息确认失败");
        }
    }

}
