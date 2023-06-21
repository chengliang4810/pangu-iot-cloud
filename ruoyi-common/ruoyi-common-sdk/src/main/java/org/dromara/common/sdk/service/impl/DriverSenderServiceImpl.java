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

package org.dromara.common.sdk.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.driver.EventConstant;
import io.github.pnoker.common.constant.driver.RabbitConstant;
import io.github.pnoker.common.dto.DriverEventDTO;
import io.github.pnoker.common.entity.DeviceEvent;
import io.github.pnoker.common.entity.point.PointValue;
import io.github.pnoker.common.enums.DeviceStatusEnum;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.driver.sdk.entity.property.DriverProperty;
import io.github.pnoker.driver.sdk.service.DriverSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverSenderServiceImpl implements DriverSenderService {

    @Resource
    private DriverProperty driverProperty;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void driverEventSender(DriverEventDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO)) {
            return;
        }

        rabbitTemplate.convertAndSend(
                RabbitConstant.TOPIC_EXCHANGE_EVENT,
                RabbitConstant.ROUTING_DRIVER_EVENT_PREFIX + driverProperty.getService(),
                entityDTO
        );
    }

    @Override
    public void deviceEventSender(DeviceEvent deviceEvent) {
        if (ObjectUtil.isNotNull(deviceEvent)) {
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_EVENT,
                    RabbitConstant.ROUTING_DEVICE_EVENT_PREFIX + driverProperty.getService(),
                    deviceEvent
            );
        }
    }

    @Override
    public void deviceStatusSender(String deviceId, DeviceStatusEnum status) {
        deviceEventSender(new DeviceEvent(deviceId, EventConstant.Device.STATUS, status));
    }

    @Override
    public void pointValueSender(PointValue pointValue) {
        if (ObjectUtil.isNotNull(pointValue)) {
            log.debug("Send point value: {}", JsonUtil.toJsonString(pointValue));
            rabbitTemplate.convertAndSend(
                    RabbitConstant.TOPIC_EXCHANGE_VALUE,
                    RabbitConstant.ROUTING_POINT_VALUE_PREFIX + driverProperty.getService(),
                    pointValue
            );
        }
    }

    @Override
    public void pointValueSender(List<PointValue> pointValues) {
        if (ObjectUtil.isNotNull(pointValues)) {
            pointValues.forEach(this::pointValueSender);
        }
    }

}
