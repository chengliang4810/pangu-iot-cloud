package com.pangu.iot.data.service;

/**
 * 设备状态服务
 *
 * @author chengliang4810
 * @date 2023/02/02 15:37
 */
public interface DeviceStatusService {

    /**
     * 设备状态变更
     *
     * @param deviceId 设备ID
     * @param status   状态
     */
    void changeStatus(String productId, String deviceId, Integer status);

    /**
     * 获得设备状态
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return int
     */
    int getStatus(String productId, String deviceId);

}
