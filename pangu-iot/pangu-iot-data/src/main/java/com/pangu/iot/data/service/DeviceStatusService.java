package com.pangu.iot.data.service;

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
     * @param productId 产品id
     * @param deviceId  设备id
     * @param clock      时间
     */
    void online(String productId, String deviceId, Integer clock);

    /**
     * 设备离线
     *
     * @param productId 产品id
     * @param deviceId  设备id
     */
    void offline(String productId, String deviceId);

    /**
     * 获得设备状态
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return int
     */
    int getStatus(String productId, String deviceId);

}
