package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.pangu.iot.manager.driver.convert.DriverConvert;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.DriverService;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.service.*;
import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.manager.api.domain.PointAttribute;
import com.pangu.manager.api.domain.dto.DriverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverSdkServiceImpl implements IDriverSdkService {

    private final DriverConvert driverConvert;
    private final IDriverService driverService;
    private final IPointInfoService pointInfoService;
    private final IDriverInfoService driverInfoService;
    private final IDriverServiceService driverServiceService;
    private final IPointAttributeService pointAttributeService;
    private final IDriverAttributeService driverAttributeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void driverRegister(DriverDTO driverDto) {
        // 注册驱动
        Driver driver = driverConvert.toEntity(driverDto);
        log.info("Register driver {}", driver);

        Driver dbDriver = driverService.selectByName(driver.getName());
        if (ObjectUtil.isNotNull(dbDriver)) {
            log.debug("Driver already registered, updating {} ", driver);
            driver.setId(dbDriver.getId());
            driverService.updateById(driver);
        } else {
            log.debug("Driver does not registered, adding {} ", driver);
            driverService.save(driver);
        }

        // register driver service
        // 新增驱动服务
        String driverServiceId = SecureUtil.md5(driver.getName() + "_" + driverDto.getHost() + "_" + driverDto.getPort());
        DriverService dbDriverService = driverServiceService.getById(driverServiceId);
        if (ObjectUtil.isNull(dbDriverService)) {
            log.debug("Driver service does not registered, adding {} ", driverServiceId);
            driverServiceService.save(new DriverService().setDriverId(driver.getId()).setId(driverServiceId).setHost(driverDto.getHost()).setPort(driverDto.getPort()).setServiceName(driverDto.getServiceName()));
        }

        //register driver attribute
        // 新驱动属性
        Map<String, DriverAttribute> newDriverAttributeMap = new HashMap<>(8);
        if (CollectionUtil.isNotEmpty(driverDto.getDriverAttribute())) {
            driverDto.getDriverAttribute().forEach(driverAttribute -> newDriverAttributeMap.put(driverAttribute.getName(), driverAttribute));
        }

        // 原有的驱动属性
        Map<String, DriverAttribute> oldDriverAttributeMap = new HashMap<>(8);
        List<DriverAttribute> byDriverId = driverAttributeService.selectByDriverId(driver.getId());
        byDriverId.forEach(driverAttribute -> oldDriverAttributeMap.put(driverAttribute.getName(), driverAttribute));


        // 对比新旧驱动属性，更新或添加
        for (String name : newDriverAttributeMap.keySet()) {
            DriverAttribute info = newDriverAttributeMap.get(name).setDriverId(driver.getId());
            if (oldDriverAttributeMap.containsKey(name)) {
                info.setId(oldDriverAttributeMap.get(name).getId());
                log.debug("Driver attribute registered, updating: {}", info);
                driverAttributeService.updateById(info);
            } else {
                log.debug("Driver attribute does not registered, adding: {}", info);
                driverAttributeService.save(info);
            }
        }

        // 删除多余的驱动属性
        for (String name : oldDriverAttributeMap.keySet()) {
            if (!newDriverAttributeMap.containsKey(name)) {
                List<DriverInfo> driverInfos = driverInfoService.selectByAttributeId(oldDriverAttributeMap.get(name).getId());
                if (CollectionUtil.isEmpty(driverInfos)) {
                    log.debug("Driver attribute is redundant, deleting: {}", oldDriverAttributeMap.get(name));
                    driverAttributeService.removeById(oldDriverAttributeMap.get(name).getId());
                    return;
                }
                log.error("The driver attribute( {} ) used by driver info and cannot be deleted", name);
            }
        }



        // register point attribute
        // 新点位属性
        Map<String, PointAttribute> newPointAttributeMap = new HashMap<>(8);
        if (CollectionUtil.isNotEmpty(driverDto.getPointAttribute())) {
            driverDto.getPointAttribute().forEach(pointAttribute -> newPointAttributeMap.put(pointAttribute.getName(), pointAttribute));
        }

        // 原有的点位属性
        Map<String, PointAttribute> oldPointAttributeMap = new HashMap<>(8);
        List<PointAttribute> pointAttributeList = pointAttributeService.selectByDriverId(driver.getId());
        pointAttributeList.forEach(pointAttribute -> oldPointAttributeMap.put(pointAttribute.getName(), pointAttribute));


        for (String name : newPointAttributeMap.keySet()) {
            PointAttribute attribute = newPointAttributeMap.get(name).setDriverId(driver.getId());
            if (oldPointAttributeMap.containsKey(name)) {
                attribute.setId(oldPointAttributeMap.get(name).getId());
                log.debug("Point attribute registered, updating: {}", attribute);
                pointAttributeService.updateById(attribute);
                continue;
            }
                log.debug("Point attribute registered, adding: {}", attribute);
                pointAttributeService.save(attribute);
        }

        for (String name : oldPointAttributeMap.keySet()) {
            if (!newPointAttributeMap.containsKey(name)) {
                List<PointInfo> pointInfos = pointInfoService.selectByAttributeId(oldPointAttributeMap.get(name).getId());
                if (CollectionUtil.isEmpty(pointInfos)) {
                    log.debug("Point attribute is redundant, deleting: {}", oldPointAttributeMap.get(name));
                    pointAttributeService.removeById(oldPointAttributeMap.get(name).getId());
                    return;
                }
                log.error("The point attribute( {} ) used by point info and cannot be deleted", name);
            }
        }

    }

}
