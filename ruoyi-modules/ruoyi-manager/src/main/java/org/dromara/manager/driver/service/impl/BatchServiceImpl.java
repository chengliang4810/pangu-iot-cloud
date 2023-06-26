package org.dromara.manager.driver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.entity.driver.DriverAttribute;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.entity.point.PointAttribute;
import org.dromara.common.iot.enums.AttributeType;
import org.dromara.manager.device.domain.vo.DeviceVo;
import org.dromara.manager.device.service.IDeviceService;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.bo.DriverAttributeValueBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeValueVo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;
import org.dromara.manager.driver.service.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final IDriverService driverService;
    private final IDeviceService deviceService;
    private final IPointAttributeService pointAttributeService;
    private final IDriverAttributeService driverAttributeService;
    private final IDriverAttributeValueService driverAttributeValueService;

    @Override
    public DriverMetadata batchDriverMetadata(String code) {
        DriverMetadata driverMetadata = new DriverMetadata();
        // 驱动信息
        Driver driver = driverService.queryByCode(code);
        driverMetadata.setDriverId(driver.getId());
        driverMetadata.setTenantId(driver.getTenantId());

        // 驱动属性
        Map<Long, DriverAttribute> driverAttributeMap = getDriverAttributeMap(driver.getId());
        driverMetadata.setDriverAttributeMap(driverAttributeMap);

        // 点位属性
        Map<Long, PointAttribute> pointAttributeMap = getPointAttributeMap(driver.getId());
        driverMetadata.setPointAttributeMap(pointAttributeMap);

        // 所有启用的网关设备
        List<DeviceVo> gatewayDeviceList = deviceService.queryDeviceListByDriverId(driver.getId(), true);
        Set<Long> gatewayDeviceIds = gatewayDeviceList.stream().map(DeviceVo::getId).collect(Collectors.toSet());

        // 网关设备的驱动属性
        Map<Long, Map<String, AttributeInfo>> driverInfoMap = getDriverInfoMap(gatewayDeviceIds, driverAttributeMap);
        driverMetadata.setDriverInfoMap(driverInfoMap);

        // 网关子设备
        Map<Long, List<Device>> deviceMap = getDeviceMap(gatewayDeviceIds);
        Set<Long> deviceIds = deviceMap.values().stream().flatMap(List::stream).map(Device::getDeviceId).collect(Collectors.toSet());
        driverMetadata.setDeviceMap(deviceMap);

//        // 网关子设备的点位属性
//        Map<String, Map<String, Point>> profilePointMap = getPointMap(deviceIds);
//        driverMetadata.setProfilePointMap(profilePointMap);

        return driverMetadata;
    }

    private Map<String, Map<String, Point>> getPointMap(Set<Long> deviceIds) {

        return null;
    }

    private Map<Long, List<Device>> getDeviceMap(Set<Long> gatewayDeviceIds) {
        Map<Long, List<Device>> deviceMap = new ConcurrentHashMap<>(16);
        gatewayDeviceIds.forEach(gatewayId -> {
            List<DeviceVo> deviceList = deviceService.queryChildDeviceListByDeviceId(gatewayId, true);
            deviceMap.put(gatewayId, deviceList.stream().map(this::convertDevice).collect(Collectors.toList()));
        });
        return deviceMap;
    }

    private Device convertDevice(DeviceVo vo) {
        Device device = new Device();
        device.setDeviceId(vo.getId());
        device.setProductId(vo.getProductId());
        device.setDeviceName(vo.getName());
        device.setDeviceCode(vo.getCode());
        return device;
    }

    private Map<Long, Map<String, AttributeInfo>> getDriverInfoMap(Set<Long> gatewayDeviceIds, Map<Long, DriverAttribute> driverAttributeMap) {
        Map<Long, Map<String, AttributeInfo>> driverInfoMap = new ConcurrentHashMap<>(16);
        gatewayDeviceIds.forEach(deviceId -> {
            Map<String, AttributeInfo> infoMap = getDriverInfoMap(deviceId, driverAttributeMap);
            if (infoMap.size() > 0) {
                driverInfoMap.put(deviceId, infoMap);
            }
        });
        return driverInfoMap;
    }

    /**
     * Get driver attribute config map
     *
     * @param deviceId           设备ID
     * @param driverAttributeMap Driver Attribute Map
     * @return map(attributeName, attributeInfo ( value, type))
     */
    public Map<String, AttributeInfo> getDriverInfoMap(Long deviceId, Map<Long, DriverAttribute> driverAttributeMap) {
        Map<String, AttributeInfo> attributeInfoMap = new ConcurrentHashMap<>(16);
        List<DriverAttributeValueVo> driverAttributeConfigs = driverAttributeValueService.queryList(new DriverAttributeValueBo().setGatewayDeviceId(deviceId));
        driverAttributeConfigs.forEach(driverInfo -> {
            DriverAttribute attribute = driverAttributeMap.get(driverInfo.getDriverAttributeId());
            attributeInfoMap.put(attribute.getAttributeName(), new AttributeInfo(driverInfo.getValue(), AttributeType.ofCode(attribute.getAttributeTypeFlag())));
        });
        return attributeInfoMap;
    }

    /**
     * 点位属性
     *
     * @param driverId 司机身份证
     * @return {@link Map}<{@link Long}, {@link PointAttribute}>
     */
    private Map<Long, PointAttribute> getPointAttributeMap(Long driverId) {
        Map<Long, PointAttribute> pointAttributeMap = new ConcurrentHashMap<>(16);
        List<PointAttributeVo> pointAttributes = pointAttributeService.selectByDriverId(driverId);
        pointAttributes.forEach(pointAttribute -> pointAttributeMap.put(pointAttribute.getId(), convertPointAttribute(pointAttribute)));
        return pointAttributeMap;
    }

    private PointAttribute convertPointAttribute(PointAttributeVo vo){
        PointAttribute pointAttribute = new PointAttribute();
        pointAttribute.setId(vo.getId());
        pointAttribute.setDisplayName(vo.getDisplayName());
        pointAttribute.setAttributeName(vo.getAttributeName());
        pointAttribute.setAttributeTypeFlag(vo.getAttributeType());
        pointAttribute.setRequired(vo.getRequired());
        pointAttribute.setDefaultValue(vo.getDefaultValue());
        pointAttribute.setDriverId(vo.getDriverId());
        pointAttribute.setRemark(vo.getRemark());
        return pointAttribute;
    }

    /**
     * 驱动属性
     *
     * @param driverId 司机身份证
     * @return {@link Map}<{@link Long}, {@link DriverAttribute}>
     */
    private Map<Long, DriverAttribute> getDriverAttributeMap(Long driverId) {
        Map<Long, DriverAttribute> driverAttributeMap = new ConcurrentHashMap<>(16);
        List<DriverAttributeVo> driverAttributes = driverAttributeService.selectByDriverId(driverId);
        driverAttributes.forEach(driverAttribute -> driverAttributeMap.put(driverAttribute.getId(), convertDriverAttribute(driverAttribute)));
        return driverAttributeMap;
    }

    private DriverAttribute convertDriverAttribute(DriverAttributeVo vo){
        DriverAttribute driverAttribute = new DriverAttribute();
        driverAttribute.setId(vo.getId());
        driverAttribute.setDisplayName(vo.getDisplayName());
        driverAttribute.setAttributeName(vo.getAttributeName());
        driverAttribute.setAttributeTypeFlag(vo.getAttributeType());
        driverAttribute.setDefaultValue(vo.getDefaultValue());
        driverAttribute.setRequired(vo.getRequired());
        driverAttribute.setDriverId(vo.getDriverId());
        driverAttribute.setRemark(vo.getRemark());
        return driverAttribute;
    }




}
