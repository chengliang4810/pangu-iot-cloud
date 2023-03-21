package com.pangu.iot.manager.driver.service;

import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;

/**
 * Notify Interface
 *
 * @author chengliang
 * @date 2023/03/08
 */
public interface INotifyService {

    /**
     * 通知所有驱动
     * TODO 暂时的通知解决方案，后续需要优化
     */
    void notifyAllDriver(Driver driver);
    /**
     * 通知驱动 新增模板(ADD) / 删除模板(DELETE) / 修改模板(UPDATE)
     *
     * @param command Operation Type
     * @param profile Profile
     */
    void notifyDriverProfile(String command, Product profile);

    /**
     * 通知驱动 新增位号(ADD) / 删除位号(DELETE) / 修改位号(UPDATE)
     *
     * @param command Operation Type
     * @param point   Point
     */
    void notifyDriverPoint(String command, DeviceAttribute point);

    /**
     * 通知驱动 新增设备(ADD) / 删除设备(DELETE) / 修改设备(UPDATE)
     *
     * @param command Operation Type
     * @param device  Device
     */
    void notifyDriverDevice(String command, Device device);

    /**
     * 通知驱动 新增驱动配置(ADD) / 删除驱动配置(DELETE) / 更新驱动配置(UPDATE)
     *
     * @param command    Operation Type
     * @param driverInfo Driver Info
     */
    void notifyDriverDriverInfo(String command, DriverInfo driverInfo);

    /**
     * 通知驱动 新增位号配置(ADD) / 删除位号配置(DELETE) / 更新位号配置(UPDATE)
     *
     * @param command   Operation Type
     * @param pointInfo PointInfo
     */
    void notifyDriverPointInfo(String command, PointInfo pointInfo);

}
