package com.pangu.common.sdk.service;

import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.manager.api.domain.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 驱动数据服务接口
 * 用户实现该接口实现数据的读取和控制
 *
 * @author chengliang4810
 * @date 2023/02/17 10:16
 */
public abstract class DriverDataService {

    protected DriverContext driverContext;

    /**
     * 读设备数据
     *
     * @return {@link List}<{@link DeviceValue}>
     */
    public String read(Device device, DeviceAttribute attribute, Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo) {
        return null;
    }

    /**
     * 读设备数据
     *
     * @param device     设备
     * @param attributes 属性
     * @return {@link DeviceValue}
     */
    public DeviceValue read(Device device, List<DeviceAttribute> attributes){

        // 驱动信息
        Map<String, AttributeInfo> driverInfo = driverContext.getDriverInfoByDeviceId(device.getId().toString());

        Map<String, String> attributesMap = attributes.parallelStream().collect(Collectors.toMap(DeviceAttribute::getKey, attribute -> {
            Map<String, AttributeInfo> pointInfo = driverContext.getPointInfoByDeviceIdAndPointId(device.getId().toString(), attribute.getId());
            return this.read(device, attribute, driverInfo, pointInfo);
        }));

        return new DeviceValue(device.getId().toString(), attributesMap);
    }

    /**
     * 控制设备
     *
     * @param deviceFunction 控制设备功能参数
     * @return {@link Boolean} 控制成功返回true，否则返回false
     */
    public abstract Boolean control(DeviceFunction deviceFunction);

    public void setDriverContext(DriverContext driverContext) {
        this.driverContext = driverContext;
    }

}
