package org.dromara.common.sdk.service;


import org.dromara.common.iot.dto.DriverEventDTO;
import org.dromara.common.iot.entity.device.DeviceEvent;
import org.dromara.common.iot.entity.point.PointValue;
import org.dromara.common.iot.enums.DeviceStatusEnum;

import java.util.List;

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
    void deviceStatusSender(String deviceId, DeviceStatusEnum status);

    /**
     * 发送位号值到消息组件
     *
     * @param pointValue PointValue
     */
    void pointValueSender(PointValue pointValue);

    /**
     * 批量发送位号值到消息组件
     *
     * @param pointValues PointValue Array
     */
    void pointValueSender(List<PointValue> pointValues);

    /**
     * 发送驱动状态
     *
     * @param driverStatus 驱动状态
     */
    void driverStatusSender(DriverEventDTO.DriverStatus driverStatus);
}
