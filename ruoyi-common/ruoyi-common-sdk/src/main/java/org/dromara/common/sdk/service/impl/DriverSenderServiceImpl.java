package org.dromara.common.sdk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.dto.DriverEventDTO;
import org.dromara.common.iot.entity.device.DeviceEvent;
import org.dromara.common.iot.entity.point.PointValue;
import org.dromara.common.iot.enums.DeviceStatusEnum;
import org.dromara.common.sdk.property.DriverProperty;
import org.dromara.common.sdk.service.DriverSenderService;
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

    @Override
    public void driverEventSender(DriverEventDTO entityDTO) {
//        if (ObjectUtil.isNull(entityDTO)) {
//            return;
//        }

//        rabbitTemplate.convertAndSend(
//                RabbitConstant.TOPIC_EXCHANGE_EVENT,
//                RabbitConstant.ROUTING_DRIVER_EVENT_PREFIX + driverProperty.getService(),
//                entityDTO
//        );
    }

    @Override
    public void deviceEventSender(DeviceEvent deviceEvent) {
//        if (ObjectUtil.isNotNull(deviceEvent)) {
//            rabbitTemplate.convertAndSend(
//                    RabbitConstant.TOPIC_EXCHANGE_EVENT,
//                    RabbitConstant.ROUTING_DEVICE_EVENT_PREFIX + driverProperty.getService(),
//                    deviceEvent
//            );
//        }
    }

    @Override
    public void deviceStatusSender(String deviceId, DeviceStatusEnum status) {
        // deviceEventSender(new DeviceEvent(deviceId, EventConstant.Device.STATUS, status));
    }

    @Override
    public void pointValueSender(PointValue pointValue) {
//        if (ObjectUtil.isNotNull(pointValue)) {
//            log.debug("Send point value: {}", JsonUtil.toJsonString(pointValue));
//            rabbitTemplate.convertAndSend(
//                    RabbitConstant.TOPIC_EXCHANGE_VALUE,
//                    RabbitConstant.ROUTING_POINT_VALUE_PREFIX + driverProperty.getService(),
//                    pointValue
//            );
//        }
    }

    @Override
    public void pointValueSender(List<PointValue> pointValues) {
//        if (ObjectUtil.isNotNull(pointValues)) {
//            pointValues.forEach(this::pointValueSender);
//        }
    }

    /**
     * 发送驱动状态
     *
     * @param driverStatus 驱动状态
     */
    @Override
    public void driverStatusSender(DriverEventDTO.DriverStatus driverStatus) {

    }
}
