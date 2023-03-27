package com.pangu.iot.data.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.domain.dto.MqttMessageDTO;
import com.pangu.common.core.enums.MqttMessageType;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.utils.EmqxUtil;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import com.pangu.manager.api.RemoteDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.pangu.common.core.constant.IotConstants.DEVICE_STATUS_CACHE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusServiceImpl implements DeviceStatusService  {

    @DubboReference
    private final RemoteDeviceService deviceService;

    /**
     * 设备上线
     *
     * @param deviceId  设备id
     * @param clock      时间
     */
    @Override
    public void online(String deviceId, Integer clock) {

        Integer value = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + deviceId);
        RedisUtils.setCacheObject(DEVICE_STATUS_CACHE_PREFIX + deviceId, clock, Duration.ofSeconds(60));
        if (ObjectUtil.isNotNull(value)){
            // 如果设备已经上线，不再重复上线
            return;
        }
        // 发送设备上线消息到emqx
        Optional.of(deviceService.getDeviceById(deviceId)).ifPresent(device -> {
            // 构建上线消息
            MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(MqttMessageType.ONLINE, deviceId, device.getName());
            EmqxUtil.getClient().publish( "iot/device/" + deviceId + "/status/" + MqttMessageType.ONLINE.getCode(), JsonUtils.toJsonString(mqttMessageDTO));
        });
    }

    /**
     * 设备离线
     *
     * @param deviceId  设备id
     */
    @Override
    public void offline(String deviceId) {
        // 获取设备最后上线时间, 如果不存在说明设备已经离线。
        // 如果存在，说明设备离线，更新设备最后上线时间
        // 还有一个原因是因为zabbix 离线的告警会多次重复，所以需要判断设备是否已经离线
        Integer clock = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + deviceId);
        if (ObjectUtil.isNotNull(clock)){
            // 将clock 转换为时间
            LocalDateTime lastOnlineTime = TimeUtil.toLocalDateTime(clock);
            log.info("设备离线，设备id：{}，最后上线时间：{}", deviceId, lastOnlineTime);
            // 调用接口，更新设备最后上线时间
            deviceService.updateDeviceLastOnlineTime(deviceId, clock);
            // 更新设备最后上线时间
            RedisUtils.deleteObject(DEVICE_STATUS_CACHE_PREFIX + deviceId);

            Optional.of(deviceService.getDeviceById(deviceId)).ifPresent(device -> {
                // 构建离线消息
                MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(MqttMessageType.OFFLINE, deviceId, device.getName());
                EmqxUtil.getClient().publish( "iot/device/" + deviceId + "/status/" + MqttMessageType.OFFLINE.getCode(), JsonUtils.toJsonString(mqttMessageDTO));
            });
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
     * 批量获得设备在线状态
     *
     * @param deviceId  设备id
     * @return int
     */
    @Override
    public Map<Long, Integer> getOnlineStatus(Set<Long> deviceId) {
        if (ObjectUtil.isEmpty(deviceId)){
            return new HashMap<>(0);
        }
        Map<Long, Integer> resultMap = new HashMap<>(deviceId.size());
        deviceId.forEach(id -> {
            Integer value = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + id);
            if (ObjectUtil.isNotNull(value)) {
                resultMap.put(id, value);
            }
        });
        return resultMap;
    }

}
