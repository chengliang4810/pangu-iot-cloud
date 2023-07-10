package org.dromara.common.sdk.service;

import org.dromara.common.iot.entity.device.DeviceValue;

/**
 * 驱动指令服务
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverCommandService {

    /**
     * 读取位号值
     *
     * @param deviceId        设备ID
     * @param gatewayDeviceId 网关设备id
     * @return 位号值
     */
    DeviceValue read(Long gatewayDeviceId, Long deviceId);

    /**
     * 读取位号值
     * @param gatewayDeviceId 网关设备id
     * @param deviceId        设备ID
     * @param pointId         位号ID
     * @return 位号值
     */
    DeviceValue read(Long gatewayDeviceId, Long deviceId, Long pointId);

//    /**
//     * 指令读取位号值
//     *
//     * @param commandDTO {@link DeviceCommandDTO}
//     */
//    void read(DeviceCommandDTO commandDTO);

    /**
     * 写取位号值
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @param value    位号值
     * @return 是否写成功
     */
    Boolean write(Long deviceId, Long pointId, String value);

//    /**
//     * 指令写取位号值
//     *
//     * @param commandDTO {@link  DeviceCommandDTO}
//     */
//    void write(DeviceCommandDTO commandDTO);

}
