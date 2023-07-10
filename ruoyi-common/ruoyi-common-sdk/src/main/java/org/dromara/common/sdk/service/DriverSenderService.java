package org.dromara.common.sdk.service;


import org.dromara.common.iot.dto.DriverEventDTO;
import org.dromara.common.iot.entity.device.DeviceEvent;
import org.dromara.common.iot.entity.device.DeviceStatus;
import org.dromara.common.iot.entity.device.DeviceValue;
import org.dromara.common.iot.enums.DeviceStatusEnum;

import java.util.concurrent.TimeUnit;

/**
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverSenderService {

    /**
     * 发送驱动事件
     *
     * @param entityDTO DriverEventDTO
     */
    void driverEventSender(DriverEventDTO entityDTO);

    /**
     * 发送设备事件
     *
     * @param deviceEvent Device Event
     */
    void deviceEventSender(DeviceEvent deviceEvent);

    /**
     * 发送设备状态事件
     *
     * @param deviceId 设备ID
     * @param status   StatusEnum
     */
    void deviceStatusSender(Long deviceId, DeviceStatusEnum status);

    /**
     * 发送设备状态事件
     *
     * @param deviceId 设备ID
     * @param status   StatusEnum
     * @param time     时间
     * @param timeUnit 时间单位
     */
    void deviceStatusSender(Long deviceId, DeviceStatusEnum status, long time, TimeUnit timeUnit);

    /**
     * 发送设备状态
     *
     * @param deviceStatus 设备状态
     */
    void deviceStatusSender(DeviceStatus deviceStatus);

    /**
     * 发送位号值到消息组件
     *
     * @param deviceValue PointValue
     */
    void pointValueSender(DeviceValue deviceValue);

    /**
     * 发送驱动状态
     *
     * @param driverStatus 驱动状态
     */
    void driverStatusSender(DriverEventDTO.DriverStatus driverStatus);
}
