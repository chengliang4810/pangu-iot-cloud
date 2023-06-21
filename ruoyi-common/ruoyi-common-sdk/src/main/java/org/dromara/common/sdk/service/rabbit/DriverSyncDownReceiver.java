/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.common.sdk.service.rabbit;

import cn.hutool.core.util.ObjectUtil;
import com.rabbitmq.client.Channel;
import io.github.pnoker.common.dto.DriverSyncDownDTO;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.driver.sdk.service.DriverSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接收驱动同步
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverSyncDownReceiver {

    @Resource
    private DriverSyncService driverSyncService;

    @RabbitHandler
    @RabbitListener(queues = "#{syncDownQueue.name}")
    public void driverSyncDownReceive(Channel channel, Message message, DriverSyncDownDTO entityDTO) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.debug("Receive driver sync down: {}", JsonUtil.toPrettyJsonString(entityDTO));
            if (ObjectUtil.isNull(entityDTO)) {
                log.error("Invalid driver sync down: {}", entityDTO);
                return;
            }

            driverSyncService.down(entityDTO);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
