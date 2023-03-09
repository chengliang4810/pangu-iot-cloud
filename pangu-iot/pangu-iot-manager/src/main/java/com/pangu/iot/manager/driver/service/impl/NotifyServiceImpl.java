package com.pangu.iot.manager.driver.service.impl;

import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.service.INotifyService;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements INotifyService {

    private final EmqxClient emqxClient;

    /**
     * 通知所有驱动
     * TODO 暂时的通知解决方案，后续需要优化
     */
    @Override
    public void notifyAllDriver(Driver driver) {
        emqxClient.publish("pangu/iot/driver/" + driver.getName() + "/metadata/sync", "1".getBytes(), 0, false);
        log.info("Notify Driver[{}] Success", driver.getName());
    }

    /**
     * 通知驱动 新增模板(ADD) / 删除模板(DELETE) / 修改模板(UPDATE)
     *
     * @param command Operation Type
     * @param profile Profile
     */
    @Override
    public void notifyDriverProfile(String command, Product profile) {

    }

    /**
     * 通知驱动 新增位号(ADD) / 删除位号(DELETE) / 修改位号(UPDATE)
     *
     * @param command Operation Type
     * @param point   Point
     */
    @Override
    public void notifyDriverPoint(String command, DeviceAttribute point) {

    }

    /**
     * 通知驱动 新增设备(ADD) / 删除设备(DELETE) / 修改设备(UPDATE)
     *
     * @param command Operation Type
     * @param device  Device
     */
    @Override
    public void notifyDriverDevice(String command, Device device) {

    }

    /**
     * 通知驱动 新增驱动配置(ADD) / 删除驱动配置(DELETE) / 更新驱动配置(UPDATE)
     *
     * @param command    Operation Type
     * @param driverInfo Driver Info
     */
    @Override
    public void notifyDriverDriverInfo(String command, DriverInfo driverInfo) {

    }

    /**
     * 通知驱动 新增位号配置(ADD) / 删除位号配置(DELETE) / 更新位号配置(UPDATE)
     *
     * @param command   Operation Type
     * @param pointInfo PointInfo
     */
    @Override
    public void notifyDriverPointInfo(String command, PointInfo pointInfo) {

    }
}
