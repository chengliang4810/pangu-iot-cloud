package com.pangu.common.zabbix.domain;

import com.pangu.common.zabbix.model.ZbxProblem;
import com.rabbitmq.client.Channel;
import lombok.experimental.Accessors;
import org.springframework.amqp.core.Message;

/**
 * 问题信息
 *
 * @author chengliang
 * @date 2023/03/13
 */
@Accessors(chain = true)
public class ProblemMessage extends ZabbixMessage<ZbxProblem>{

    public ProblemMessage(Channel channel, Message message, ZbxProblem zbxProblem) {
        super(channel, message);
        this.data = zbxProblem;
    }

}
