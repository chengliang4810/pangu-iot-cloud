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
import io.github.pnoker.common.dto.DriverMetadataDTO;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.driver.sdk.service.DriverMetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接收驱动元数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverMetadataReceiver {

    @Resource
    private DriverMetadataService driverMetadataService;

    @RabbitHandler
    @RabbitListener(queues = "#{driverMetadataQueue.name}")
    public void driverMetadataReceive(Channel channel, Message message, DriverMetadataDTO entityDTO) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
            log.debug("Receive driver metadata: {}", JsonUtil.toPrettyJsonString(entityDTO));
            if (ObjectUtil.isNull(entityDTO)
                    || ObjectUtil.isNull(entityDTO.getType())
                    || ObjectUtil.isNull(entityDTO.getMetadataCommandType())) {
                log.error("Invalid driver metadata: {}", entityDTO);
                return;
            }

            switch (entityDTO.getType()) {
                case PROFILE:
                    driverMetadataService.profileMetadata(entityDTO);
                    break;
                case POINT:
                    driverMetadataService.pointMetadata(entityDTO);
                    break;
                case DEVICE:
                    driverMetadataService.deviceMetadata(entityDTO);
                    break;
                case DRIVER_ATTRIBUTE_CONFIG:
                    driverMetadataService.driverInfoMetadata(entityDTO);
                    break;
                case POINT_ATTRIBUTE_CONFIG:
                    driverMetadataService.pointInfoMetadata(entityDTO);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
