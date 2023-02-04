package com.pangu.iot.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.pangu.common.core.constant.IotConstants.DEVICE_STATUS_CACHE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusServiceImpl implements DeviceStatusService  {

    /**
     * 设备上线
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @param clock      时间
     */
    @Override
    public void online(String productId, String deviceId, Integer clock) {
        RedisUtils.setCacheObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId, clock);
    }

    /**
     * 设备离线
     *
     * @param productId 产品id
     * @param deviceId  设备id
     */
    @Override
    public void offline(String productId, String deviceId) {
        Integer clock = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId);
        if (ObjectUtil.isNotNull(clock)){
            // 将clock 转换为时间
            LocalDateTime lastOnlineTime = TimeUtil.toLocalDateTime(clock);
            log.info("设备离线，设备id：{}，产品id：{}，最后上线时间：{}", deviceId, productId, lastOnlineTime);
            // 更新设备最后上线时间
            RedisUtils.deleteObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId);
        }
    }

    /**
     * 获得设备状态
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return int
     */
    @Override
    public int getStatus(String productId, String deviceId) {
        Integer status = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId);
        return status == null ? 0 : status;
    }
}
