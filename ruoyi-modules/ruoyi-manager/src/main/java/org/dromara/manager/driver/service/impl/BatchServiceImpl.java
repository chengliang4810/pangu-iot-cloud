package org.dromara.manager.driver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.DriverAttribute;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.entity.point.PointAttribute;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;
import org.dromara.manager.driver.service.BatchService;
import org.dromara.manager.driver.service.IDriverAttributeService;
import org.dromara.manager.driver.service.IDriverService;
import org.dromara.manager.driver.service.IPointAttributeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final IDriverService driverService;
    private final IPointAttributeService pointAttributeService;
    private final IDriverAttributeService driverAttributeService;

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

        return driverMetadata;
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
