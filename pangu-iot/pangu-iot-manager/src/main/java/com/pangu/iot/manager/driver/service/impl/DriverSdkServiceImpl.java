package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.iot.manager.device.domain.GatewayDeviceBind;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.device.service.IGatewayDeviceBindService;
import com.pangu.iot.manager.driver.convert.DriverConvert;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.DriverService;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.service.*;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import com.pangu.manager.api.domain.*;
import com.pangu.manager.api.domain.dto.DriverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverSdkServiceImpl implements IDriverSdkService {

    private final DriverConvert driverConvert;
    private final IProductService productService;
    private final IDriverService driverService;
    private final IDeviceService deviceService;
    private final IPointInfoService pointInfoService;
    private final IDriverInfoService driverInfoService;
    private final IDriverServiceService driverServiceService;
    private final IProductServiceService productServiceService;
    private final IPointAttributeService pointAttributeService;
    private final IDriverAttributeService driverAttributeService;
    private final IDeviceAttributeService deviceAttributeService;
    private final IGatewayDeviceBindService gatewayDeviceBindService;


    /**
     * ???????????????????????????
     *
     * @param primaryKey ??????
     */
    @Override
    public DriverMetadata driverMetadataSync(String primaryKey) {
        DriverMetadata driverMetadata = new DriverMetadata();
        // ??????????????????
        Driver driver = driverService.selectByName(primaryKey);
        if (ObjectUtil.isNull(driver)) {
            log.error("Driver does not exist, primaryKey: {}", primaryKey);
            return driverMetadata;
        }

        driverMetadata.setDriverId(driver.getId());

        // ??????????????????
        Map<Long, DriverAttribute> driverAttributeMap = driverAttributeService.getDriverAttributeMap(driver.getId());
        driverMetadata.setDriverAttributeMap(driverAttributeMap);
        // ??????????????????
        Map<Long, PointAttribute> pointAttributeMap = pointAttributeService.getPointAttributeMap(driver.getId());
        driverMetadata.setPointAttributeMap(pointAttributeMap);

        // ???????????????????????????????????? ??????????????????????????????
        List<Long> productIds = productService.listByDriverId(driver.getId());
        List<Device> gatewayDeviceList = deviceService.list(Wrappers.lambdaQuery(Device.class).in(Device::getProductId, productIds));
        Set<Long> gatewayDeviceIds = gatewayDeviceList.stream().map(Device::getId).collect(Collectors.toSet());

        // ???????????????????????????????????? ???????????????????????????
        List<GatewayDeviceBind> gatewayDeviceBindList = gatewayDeviceBindService.list(Wrappers.lambdaQuery(GatewayDeviceBind.class).in(GatewayDeviceBind::getGatewayDeviceId, gatewayDeviceIds));
        Set<Long> childDeviceIds = gatewayDeviceBindList.stream().map(GatewayDeviceBind::getDeviceId).collect(Collectors.toSet());
        List<Device> deviceList = deviceService.list(Wrappers.lambdaQuery(Device.class).in(Device::getId, childDeviceIds));

        if (CollectionUtil.isEmpty(deviceList)) {
            log.error("Device does not exist, driverId: {}", driver.getId());
            return driverMetadata;
        }

        // ????????????ID??????
        Map<Long, List<GatewayDeviceBind>> gatewayGroupMap = gatewayDeviceBindList.stream().collect(Collectors.groupingBy(GatewayDeviceBind::getGatewayDeviceId));

        // ??????ID
        Set<Long> deviceIds = deviceList.stream().map(Device::getId).collect(Collectors.toSet());

        // ????????????????????????
        Map<Long, Map<String, AttributeInfo>> gatewayDriverInfoMap = driverInfoService.getDriverInfoMap(gatewayDeviceIds, driverAttributeMap);
        Map<Long, Map<String, AttributeInfo>> driverInfoMap = new HashMap<>(deviceIds.size());
        gatewayDriverInfoMap.forEach((gatewayId, value) ->{
            List<GatewayDeviceBind> gatewayDeviceBinds = gatewayGroupMap.get(gatewayId);
            if (CollectionUtil.isEmpty(gatewayDeviceBinds)) {
               return;
            }
            gatewayDeviceBinds.forEach(gatewayDeviceBind -> {
                driverInfoMap.put(gatewayDeviceBind.getDeviceId(), value);
            });
        });
        driverMetadata.setDriverInfoMap(driverInfoMap);

        // DeviceMap
        Map<Long, Device> deviceMap = deviceList.stream().collect(Collectors.toMap(Device::getId, device -> device));
        driverMetadata.setDeviceMap(deviceMap);
        // ????????????
        Map<Long, Map<Long, DeviceAttribute>> profileAttributeMap = deviceAttributeService.getProfileAttributeMap(deviceIds);
        driverMetadata.setProfileAttributeMap(profileAttributeMap);

        // ??????????????????
        Map<Long, Map<Long, Map<String, AttributeInfo>>> devicePointInfoMap = pointInfoService.getPointInfoMap(deviceList, profileAttributeMap, pointAttributeMap);

        // ????????????
        Map<Long, Map<Long, ProductService>> profileServiceMap = productServiceService.getProfileServiceMap(deviceIds);
        driverMetadata.setProfileServiceMap(profileServiceMap);
        log.debug("profileServiceMap: {}", profileServiceMap);

        // ??????????????????
        Map<Long, Map<Long, Map<String, AttributeInfo>>> devicePointInfoMapByService = pointInfoService.getPointInfoMapByProductService(deviceList, profileServiceMap, pointAttributeMap);
        log.debug("devicePointInfoMapByService: {}", devicePointInfoMapByService);
        // ??????
        devicePointInfoMap.forEach((deviceId, pointInfoMap) -> {
            Map<Long, Map<String, AttributeInfo>> servicePointInfoMap = devicePointInfoMapByService.get(deviceId);
            if (MapUtil.isNotEmpty(servicePointInfoMap)) {
                pointInfoMap.putAll(servicePointInfoMap);
            }
        });

        driverMetadata.setPointInfoMap(devicePointInfoMap);

        return driverMetadata;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void driverRegister(DriverDTO driverDto) {
        // ????????????
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
        // ??????????????????
        String driverServiceId = SecureUtil.md5(driver.getName() + "_" + driverDto.getHost() + "_" + driverDto.getPort());
        DriverService dbDriverService = driverServiceService.getById(driverServiceId);
        if (ObjectUtil.isNull(dbDriverService)) {
            log.debug("Driver service does not registered, adding {} ", driverServiceId);
            driverServiceService.save(new DriverService().setDriverId(driver.getId()).setId(driverServiceId).setHost(driverDto.getHost()).setPort(driverDto.getPort()).setServiceName(driverDto.getServiceName()));
        }

        //register driver attribute
        // ???????????????
        Map<String, DriverAttribute> newDriverAttributeMap = new HashMap<>(8);
        if (CollectionUtil.isNotEmpty(driverDto.getDriverAttribute())) {
            driverDto.getDriverAttribute().forEach(driverAttribute -> newDriverAttributeMap.put(driverAttribute.getName(), driverAttribute));
        }

        // ?????????????????????
        Map<String, DriverAttribute> oldDriverAttributeMap = new HashMap<>(8);
        List<DriverAttribute> byDriverId = driverAttributeService.selectByDriverId(driver.getId());
        byDriverId.forEach(driverAttribute -> oldDriverAttributeMap.put(driverAttribute.getName(), driverAttribute));


        // ??????????????????????????????????????????
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

        // ???????????????????????????
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
        // ???????????????
        Map<String, PointAttribute> newPointAttributeMap = new HashMap<>(8);
        if (CollectionUtil.isNotEmpty(driverDto.getPointAttribute())) {
            driverDto.getPointAttribute().forEach(pointAttribute -> newPointAttributeMap.put(pointAttribute.getName(), pointAttribute));
        }

        // ?????????????????????
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
