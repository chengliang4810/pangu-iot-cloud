package org.dromara.common.sdk;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.device.DeviceStatus;
import org.dromara.common.iot.enums.DeviceStatusEnum;
import org.dromara.common.sdk.service.DriverSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 网关状态上下文
 * 有需要可替换为redis或其他缓存
 * @author chengliang
 * @date 2023/06/25
 */
@Slf4j
@Component
public class GatewayStatusContext {

    private static DriverSenderService driverSenderService = null;

    /**
     * 网关状态
     * key: deviceId
     * value: DeviceStatusEnum
     * 默认30秒过期
     */
    private static final TimedCache<Long, DeviceStatusEnum> GATEWAY_STATUS_MAP = CacheUtil.newTimedCache(31000);

    public GatewayStatusContext(@Autowired DriverSenderService driverSenderService) {
        GatewayStatusContext.driverSenderService = driverSenderService;
        // 定时任务，每秒执行一次
        GATEWAY_STATUS_MAP.schedulePrune(1000);
    }

    /**
     * 设置状态
     *
     * @param deviceId 设备id
     * @param status   状态
     */
    public static void setStatus(Long deviceId, DeviceStatusEnum status) {
       setStatus(DeviceStatus.of(deviceId, status, DeviceStatus.DEFAULT_TIME, DeviceStatus.DEFAULT_UNIT));
    }

    /**
     * 设置状态
     *
     * @param deviceId 设备id
     * @param status   状态
     * @param time     cache时间 单位秒
     */
    public static void setStatus(Long deviceId, DeviceStatusEnum status, long time) {
        setStatus(DeviceStatus.of(deviceId, status, time, DeviceStatus.DEFAULT_UNIT));
    }

    /**
     * 设置状态
     *
     * @param deviceId 设备id
     * @param status   状态
     * @param time     cache时间 单位秒
     */
    public static void setStatus(Long deviceId, DeviceStatusEnum status, long time, TimeUnit timeUnit) {
        setStatus(DeviceStatus.of(deviceId, status, time, timeUnit));
    }

    /**
     * 设置状态
     *
     * @param deviceStatus 设备状态
     */
    public static void setStatus(DeviceStatus deviceStatus) {
        if (ObjectUtil.isNull(deviceStatus)){
            return;
        }
        GATEWAY_STATUS_MAP.put(deviceStatus.getDeviceId(), deviceStatus.getStatus(), deviceStatus.getTimeUnit().toMillis(deviceStatus.getTime()));
        driverSenderService.deviceStatusSender(deviceStatus);
    }

    /**
     * 获取状态
     *
     * @param deviceId 设备id
     * @return 状态
     */
    public static DeviceStatusEnum getStatus(Long deviceId) {
        return GATEWAY_STATUS_MAP.get(deviceId);
    }

    /**
     * 移除状态
     * @param deviceId 设备id
     */
    public static void removeStatus(Long deviceId) {
        GATEWAY_STATUS_MAP.remove(deviceId);
        driverSenderService.deviceStatusSender(DeviceStatus.of(deviceId, DeviceStatusEnum.OFFLINE));
    }

}
