package com.pangu.iot.data.service;

import java.util.Map;
import java.util.Set;

/**
 * 设备状态服务
 *
 * @author chengliang4810
 * @date 2023/02/02 15:37
 */
public interface DeviceStatusService {

    /**
     * 设备上线
     *
     * @param deviceId  设备id
     * @param clock      时间
     */
    void online(String deviceId, Integer clock);

    /**
     * 设备离线
     *
     * @param deviceId  设备id
     */
    void offline(String deviceId);

    /**
     * 获得设备在线状态
     *
     * @param deviceId  设备id
     * @return int
     */
    boolean getOnlineStatus(String deviceId);

    /**
     * 批量获取设备在线状态
     *
     * @param deviceId 设备id
     * @return {@link Map}<{@link String}, {@link Boolean}>
     */
    Map<String, Boolean> getOnlineStatus(Set<?> deviceId);

}
