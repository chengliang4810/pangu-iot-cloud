package org.dromara.common.sdk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.utils.EmqxUtil;
import org.dromara.common.iot.constant.DeviceAttributeTopic;
import org.dromara.common.iot.constant.DeviceTopic;
import org.dromara.common.iot.dto.DriverEventDTO;
import org.dromara.common.iot.entity.device.DeviceEvent;
import org.dromara.common.iot.entity.device.DeviceStatus;
import org.dromara.common.iot.entity.device.DeviceValue;
import org.dromara.common.iot.enums.DeviceStatusEnum;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.sdk.property.DriverProperty;
import org.dromara.common.sdk.service.DriverSenderService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverSenderServiceImpl implements DriverSenderService {

    private final DriverProperty driverProperty;


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

    /**
     * 发送设备状态
     * 状态保存时间采用默认 30秒
     *
     * @param deviceId 设备id
     * @param status   状态
     */
    @Override
    public void deviceStatusSender(Long deviceId, DeviceStatusEnum status) {
        this.deviceStatusSender(deviceId, status, 30, TimeUnit.SECONDS);
    }

    /**
     * 发送设备状态事件
     *
     * @param deviceId 设备ID
     * @param status   StatusEnum
     * @param time     时间
     * @param timeUnit 时间单位
     */
    @Override
    public void deviceStatusSender(Long deviceId, DeviceStatusEnum status, long time, TimeUnit timeUnit) {
        this.deviceStatusSender(DeviceStatus.of(deviceId, status, time, timeUnit));
    }

    /**
     * 发送设备状态
     *
     * @param deviceStatus 设备状态
     */
    @Override
    public void deviceStatusSender(DeviceStatus deviceStatus) {
        if (null != deviceStatus) {
            EmqxUtil.publish(DeviceTopic.getDeviceStatusTopic(deviceStatus.getDeviceId()), JsonUtils.toJsonString(deviceStatus));
        }
    }

    @Override
    public void pointValueSender(DeviceValue deviceValue) {
        if (ObjectUtil.isNotNull(deviceValue) && CollUtil.isNotEmpty(deviceValue.getAttributes())) {
            String jsonString = JsonUtils.toJsonString(deviceValue);
            log.debug("Send point values: {}", jsonString);
            EmqxUtil.publish(DeviceAttributeTopic.getDeviceAttributeReportTopic(deviceValue.getDeviceCode()), jsonString);
        }
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
