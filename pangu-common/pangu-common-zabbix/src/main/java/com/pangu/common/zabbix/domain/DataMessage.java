package com.pangu.common.zabbix.domain;

import com.pangu.common.zabbix.model.ZbxValue;
import com.rabbitmq.client.Channel;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.amqp.core.Message;

/**
 * 数据信息
 *
 * @author chengliang
 * @date 2023/03/13
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DataMessage extends ZabbixMessage<ZbxValue> {

    public DataMessage(Channel channel, Message message, ZbxValue data) {
        super(channel, message);
        this.data = data;
    }

}


