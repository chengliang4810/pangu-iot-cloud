package org.dromara.common.sdk.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.device.DeviceStatus;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.GatewayStatusContext;
import org.dromara.common.sdk.service.DriverCustomService;
import org.dromara.common.sdk.service.GatewayStatusService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayStatusServiceImpl  implements GatewayStatusService {

    private final DriverContext driverContext;
    private final DriverCustomService driverCustomService;


    @Override
    public void status(Long deviceId) {
        // 网关状态本地缓存
        Device device = driverContext.getDeviceByDeviceId(deviceId);
        Map<String, AttributeInfo> driverInfo = driverContext.getDriverInfoByDeviceId(deviceId);
        try {
            DeviceStatus deviceStatus = driverCustomService.gatewayStatus(driverInfo, device);
            // 网关状态上报
            GatewayStatusContext.setStatus(deviceId, deviceStatus.getStatus(), deviceStatus.getTime(), deviceStatus.getTimeUnit());
        } catch (Exception e) {
            log.warn("网关设备状态调度任务异常", e);
            // 出现异常则认为网关离线
            GatewayStatusContext.removeStatus(deviceId);
        }
    }

}
