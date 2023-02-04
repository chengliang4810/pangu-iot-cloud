package com.pangu.iot.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.pangu.common.core.constant.IotConstants.DEVICE_STATUS_CACHE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusServiceImpl implements DeviceStatusService  {

    /**
     * 设备上线
     *
     * @param deviceId  设备id
     * @param clock      时间
     */
    @Override
    public void online(String deviceId, Integer clock) {
        RedisUtils.setCacheObject(DEVICE_STATUS_CACHE_PREFIX + deviceId, clock);
    }

    /**
     * 设备离线
     *
     * @param deviceId  设备id
     */
    @Override
    public void offline(String deviceId) {
        Integer clock = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + deviceId);
        if (ObjectUtil.isNotNull(clock)){
            // 将clock 转换为时间
            LocalDateTime lastOnlineTime = TimeUtil.toLocalDateTime(clock);
            log.info("设备离线，设备id：{}，最后上线时间：{}", deviceId, lastOnlineTime);
            // 更新设备最后上线时间
            RedisUtils.deleteObject(DEVICE_STATUS_CACHE_PREFIX + deviceId);
        }
    }

    /**
     * 获得设备状态
     *
     * @param deviceId  设备id
     * @return int
     */
    @Override
    public boolean getOnlineStatus(String deviceId) {
        return RedisUtils.hasKey(DEVICE_STATUS_CACHE_PREFIX + deviceId);
    }

    /**
     * 获得设备状态
     *
     * @param deviceId  设备id
     * @return int
     */
    @Override
    public Map<String, Boolean> getOnlineStatus(Set<?> deviceId) {
        if (ObjectUtil.isEmpty(deviceId)){
            return Collections.EMPTY_MAP;
        }
        Map<String, Boolean> resultMap = new HashMap<>(deviceId.size());
        deviceId.forEach(id -> resultMap.put(id.toString(), RedisUtils.hasKey(DEVICE_STATUS_CACHE_PREFIX + id)));
        return resultMap;
    }

}
