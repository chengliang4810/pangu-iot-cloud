package com.pangu.data.api;

import java.util.Map;
import java.util.Set;

/**
 * 设备状态读取服务
 *
 * @author chengliang4810
 */
public interface RemoteDeviceStatusService {

    /**
     * 通过设备ID获取设备在线状态以及时间
     */
    Map<Long, Integer> getDeviceOnlineStatus(Set<Long> deviceCode);

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

}
