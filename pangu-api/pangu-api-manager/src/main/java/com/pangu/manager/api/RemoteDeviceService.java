package com.pangu.manager.api;

import com.pangu.manager.api.domain.Device;

/**
 * 设备相关服务
 *
 * @author chengliang4810
 */
public interface RemoteDeviceService {


    /**
     * 更新设备最后上线时间
     *
     * @param deviceCode 设备代码
     * @param ns       时间
     */
    void updateDeviceLastOnlineTime(String deviceCode, int ns);


    /**
     * 被代码设备id
     *
     * @param deviceCode 设备代码
     */
    Long getDeviceIdByCode(String deviceCode);


    /**
     * 通过id获取设备
     *
     * @param deviceId 设备id
     * @return {@link Device}
     */
    Device getDeviceById(String deviceId);

}
