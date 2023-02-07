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
    Map<String, Integer> getDeviceOnlineStatus(Set<String> deviceCode);

}
