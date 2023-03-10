package com.pangu.common.sdk.service;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public abstract class DriverDataService {

    protected DriverContext driverContext;

    /**
     * 读取设备单个属性数据
     * @return {@link List}<{@link DeviceValue}>
     */
    public String read(Device device, DeviceAttribute attribute, Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo) throws Exception{
        return "";
    }

    /**
     * 读设备所有属性数据
     *
     * @param device     设备
     * @param attributes 属性
     * @return {@link DeviceValue}
     */

    public DeviceValue read(Device device, List<DeviceAttribute> attributes) throws Exception {

        // 驱动信息
        Map<String, AttributeInfo> driverInfo = driverContext.getDriverInfoByDeviceId(device.getId());

        // 读取设备属性数据
        Map<String, String> attributesMap = attributes.parallelStream().collect(Collectors.toMap(DeviceAttribute::getKey, attribute -> {
            Map<String, AttributeInfo> pointInfo = driverContext.getPointInfoByDeviceIdAndPointId(device.getId(), attribute.getId());
            try {
                return this.read(device, attribute, driverInfo, pointInfo);
            } catch (Exception e) {
                log.debug("Read Value Error", e);
                throw new ServiceException("读取设备数据失败: {}" + e.getMessage());
            }
        }));

        return new DeviceValue(device.getCode(), attributesMap);
    }

    /**
     * 控制设备
     *
     * @param deviceFunction 控制设备功能参数
     * @return {@link Boolean} 控制成功返回true，否则返回false
     */
    public Boolean control(DeviceFunction deviceFunction) throws Exception {
        throw new ServiceException("该驱动不支持控制设备");
    }

    public void setDriverContext(DriverContext driverContext) {
        this.driverContext = driverContext;
    }

}
