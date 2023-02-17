package com.pangu.common.sdk.service;

import com.pangu.common.zabbix.model.DeviceValue;

import java.util.List;

/**
 * 驱动数据服务接口
 * 用户实现该接口实现数据的读取和控制
 *
 * @author chengliang4810
 * @date 2023/02/17 10:16
 */
public interface DriverDataService {

    /**
     * 读设备数据
     *
     * @return {@link List}<{@link DeviceValue}>
     */
    List<DeviceValue> read();


    /**
     * 控制设备
     */
    void control();

}
