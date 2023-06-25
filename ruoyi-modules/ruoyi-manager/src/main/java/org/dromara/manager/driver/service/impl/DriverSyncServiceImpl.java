package org.dromara.manager.driver.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.utils.EmqxUtil;
import org.dromara.common.iot.constant.DriverTopic;
import org.dromara.common.iot.dto.DriverDTO;
import org.dromara.common.iot.dto.DriverSyncDownDTO;
import org.dromara.common.iot.dto.DriverSyncUpDTO;
import org.dromara.common.iot.entity.driver.DriverAttribute;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.entity.point.PointAttribute;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.bo.DriverApplicationBo;
import org.dromara.manager.driver.domain.bo.DriverAttributeBo;
import org.dromara.manager.driver.domain.bo.DriverBo;
import org.dromara.manager.driver.domain.bo.PointAttributeBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.domain.vo.DriverVo;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;
import org.dromara.manager.driver.service.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverSyncServiceImpl implements DriverSyncService {

    private final BatchService batchService;
    private final IDriverService driverService;
    private final IPointAttributeService pointAttributeService;
    private final IDriverAttributeService driverAttributeService;
    private final IDriverApplicationService driverApplicationService;
    private final IPointAttributeValueService pointAttributeValueService;
    private final IDriverAttributeValueService driverAttributeValueService;

    /**
     * 驱动注册上线
     *
     * @param entityDTO 实体dto
     */
    @Override
    public void up(DriverSyncUpDTO entityDTO) {

        if (ObjectUtil.isNull(entityDTO) || ObjectUtil.isNull(entityDTO.getDriver())) {
            return;
        }

        // 注册驱动基础信息
        DriverVo driverVo = registerDriver(entityDTO);
        // 注册驱动服务应用
        registerDriverApplication(driverVo, entityDTO);
        // 注册驱动属性
        registerDriverAttribute(entityDTO, driverVo);
        // 注册点位属性
        registerPointAttribute(entityDTO, driverVo);

        DriverMetadata driverMetadata2 = batchService.batchDriverMetadata(driverVo.getCode());

        DriverMetadata driverMetadata = new DriverMetadata();

        DriverSyncDownDTO driverSyncDownDTO = new DriverSyncDownDTO(JsonUtils.toJsonString(driverMetadata));

        EmqxUtil.getClient().publish(DriverTopic.getDriverRegisterBackTopic(entityDTO.getDriverUniqueKey()), JsonUtils.toJsonString(driverSyncDownDTO));
    }

    /**
     * 注册点属性
     *
     * @param driverSyncUpDTO driverSyncUpDTO
     * @param driverVo  司机签证官
     */
    private void registerPointAttribute(DriverSyncUpDTO driverSyncUpDTO, DriverVo driverVo) {

        // 新驱动属性
        Map<String, PointAttribute> newPointAttributeMap = new HashMap<>(8);
        if (ObjectUtil.isNotNull(driverSyncUpDTO.getPointAttributes()) && !driverSyncUpDTO.getPointAttributes().isEmpty()) {
            driverSyncUpDTO.getPointAttributes().forEach(pointAttribute -> newPointAttributeMap.put(pointAttribute.getAttributeName(), pointAttribute));
        }

        // 旧驱动属性
        Map<String, PointAttributeVo> oldPointAttributeMap = new HashMap<>(8);
        List<PointAttributeVo> byDriverId = pointAttributeService.selectByDriverId(driverVo.getId());
        byDriverId.forEach(pointAttribute -> oldPointAttributeMap.put(pointAttribute.getAttributeName(), pointAttribute));


        for (Map.Entry<String, PointAttribute> entry : newPointAttributeMap.entrySet()) {
            String name = entry.getKey();
            PointAttribute attribute = newPointAttributeMap.get(name);
            attribute.setDriverId(driverVo.getId());
            PointAttributeBo pointAttributeBo = convertPointAttribute(attribute);
            if (oldPointAttributeMap.containsKey(name)) {
                attribute.setId(oldPointAttributeMap.get(name).getId());
                pointAttributeBo.setId(attribute.getId());
                log.debug("Point attribute registered, updating: {}", attribute);
                pointAttributeService.updateByBo(pointAttributeBo);
            } else {
                log.debug("Point attribute registered, adding: {}", attribute);
                pointAttributeService.insertByBo(pointAttributeBo);
            }
        }

        for (Map.Entry<String, PointAttributeVo> entry : oldPointAttributeMap.entrySet()) {
            String name = entry.getKey();
            if (!newPointAttributeMap.containsKey(name)) {
                Boolean exist = pointAttributeValueService.existByAttributeId(oldPointAttributeMap.get(name).getId());
                if (BooleanUtil.isTrue(exist)) {
                    log.warn("Point attribute({}) is used by point attribute config and cannot be deleted", name);
                    continue;
                }

                pointAttributeService.deleteWithValidByIds(Collections.singletonList(oldPointAttributeMap.get(name).getId()), false);
            }
        }
    }

    private PointAttributeBo convertPointAttribute(PointAttribute pointAttribute) {
        PointAttributeBo pointAttributeBo = new PointAttributeBo();

        pointAttributeBo.setDriverId(pointAttribute.getDriverId());
        pointAttributeBo.setAttributeName(pointAttribute.getAttributeName());
        pointAttributeBo.setAttributeType(pointAttribute.getAttributeTypeFlag());
        pointAttributeBo.setDisplayName(pointAttribute.getDisplayName());
        pointAttributeBo.setDefaultValue(pointAttribute.getDefaultValue());
        pointAttributeBo.setRequired(pointAttribute.getRequired());
        pointAttributeBo.setRemark(pointAttribute.getRemark());

        pointAttributeBo.setCreateBy(0L);
        pointAttributeBo.setUpdateBy(0L);
        pointAttributeBo.setCreateDept(0L);
        return pointAttributeBo;
    }

    /**
     * 注册驱动应用程序
     *
     * @param driverVo  司机签证官
     * @param entityDTO 实体dto
     */
    private void registerDriverApplication(DriverVo driverVo, DriverSyncUpDTO entityDTO) {
        DriverApplicationBo bo = new DriverApplicationBo();
        bo.setDriverId(driverVo.getId());
        bo.setTenantId(entityDTO.getTenant());
        bo.setApplicationName(entityDTO.getDriver().getServiceName());
        bo.setPort(entityDTO.getDriver().getServicePort());
        bo.setHost(entityDTO.getDriver().getServiceHost());
        Boolean exist = driverApplicationService.exist(bo);
        if (BooleanUtil.isTrue(exist)){
          return;
        }

        log.info("driver application not registered, adding {}", bo);

        bo.setCreateBy(0L);
        bo.setCreateDept(0L);
        driverApplicationService.insertByBo(bo);
    }

    /**
     * 注册驱动
     *
     * @param entityDTO DriverSyncUpDTO
     */
    private DriverVo registerDriver(DriverSyncUpDTO entityDTO) {
        // register driver
        DriverDTO driverDTO = entityDTO.getDriver();
        log.info("Register driver {}", driverDTO);
        // 检查driver是否存在
        Driver driver = driverService.queryByCode(entityDTO.getDriver().getDriverCode());
        log.info("Driver {}", driver);
        if (ObjectUtil.isNull(driver)) {
            log.info("Driver does not registered, adding {} ", driverDTO);
            DriverBo driverBo = buildDriverBo(driverDTO);
            driverService.insertByBo(driverBo);
            return driverService.queryById(driverBo.getId());
        }

        log.debug("Driver already registered, updating {} ", driverDTO);
        driverService.updateByBo(buildDriverBo(driverDTO).setId(driver.getId()));
        return driverService.queryById(driver.getId());
    }

    private DriverBo buildDriverBo(DriverDTO driverDTO) {
        DriverBo driverBo = new DriverBo();
        driverBo.setCode(driverDTO.getDriverCode());
        driverBo.setDisplayName(driverDTO.getDriverName());
        driverBo.setRemark(driverDTO.getRemark());

        driverBo.setCreateBy(0L);
        driverBo.setCreateDept(0L);
        driverBo.setUpdateBy(0L);
        return driverBo;
    }

    /**
     * 注册驱动属性
     *
     * @param driverSyncUpDTO DriverSyncUpDTO
     * @param entityDO        Driver
     */
    private void registerDriverAttribute(DriverSyncUpDTO driverSyncUpDTO, DriverVo entityDO) {

        // 新驱动属性Map
        Map<String, DriverAttribute> newDriverAttributeMap = new HashMap<>(8);
        if (CollUtil.isNotEmpty(driverSyncUpDTO.getDriverAttributes())) {
            driverSyncUpDTO.getDriverAttributes().forEach(driverAttribute -> newDriverAttributeMap.put(driverAttribute.getAttributeName(), driverAttribute));
        }

        // 原驱动属性Map
        Map<String, DriverAttributeVo> oldDriverAttributeMap = new HashMap<>(8);
        List<DriverAttributeVo> byDriverId = driverAttributeService.selectByDriverId(entityDO.getId());
        byDriverId.forEach(driverAttribute -> oldDriverAttributeMap.put(driverAttribute.getAttributeName(), driverAttribute));

        for (Map.Entry<String, DriverAttribute> entry : newDriverAttributeMap.entrySet()) {
            String name = entry.getKey();
            DriverAttribute info = newDriverAttributeMap.get(name);
            info.setDriverId(entityDO.getId());

            DriverAttributeBo driverAttributeBo = buildDriverAttributeBo(info);
            if (oldDriverAttributeMap.containsKey(name)) {
                info.setId(oldDriverAttributeMap.get(name).getId());
                log.debug("Driver attribute registered, updating: {}", info);
                driverAttributeBo.setId(info.getId());
                driverAttributeService.updateByBo(driverAttributeBo);
            } else {
                log.debug("Driver attribute does not registered, adding: {}", info);
                driverAttributeService.insertByBo(driverAttributeBo);
            }
        }

        for (Map.Entry<String, DriverAttributeVo> entry : oldDriverAttributeMap.entrySet()) {
            String name = entry.getKey();
            if (!newDriverAttributeMap.containsKey(name)) {
                Long count = driverAttributeValueService.countByAttributeId(oldDriverAttributeMap.get(name).getId());
                if (count == 0) {
                    log.debug("Driver attribute is redundant, deleting: {}", oldDriverAttributeMap.get(name));
                    driverAttributeService.deleteWithValidByIds(Collections.singletonList(oldDriverAttributeMap.get(name).getId()), false);
                    continue;
                }
                log.warn("The driver attribute(" + name + ") used by driver attribute config and cannot be deleted");
            }
        }
    }

    private DriverAttributeBo buildDriverAttributeBo(DriverAttribute driverAttribute) {
        DriverAttributeBo driverAttributeBo = new DriverAttributeBo();
        driverAttributeBo.setId(driverAttribute.getId());
        driverAttributeBo.setDriverId(driverAttribute.getDriverId());
        driverAttributeBo.setAttributeName(driverAttribute.getAttributeName());
        driverAttributeBo.setAttributeType(driverAttribute.getAttributeTypeFlag());
        driverAttributeBo.setDisplayName(driverAttribute.getDisplayName());
        driverAttributeBo.setDefaultValue(driverAttribute.getDefaultValue());
        driverAttributeBo.setRequired(driverAttribute.getRequired());
        driverAttributeBo.setRemark(driverAttributeBo.getRemark());

        driverAttributeBo.setCreateBy(0L);
        driverAttributeBo.setCreateDept(0L);
        driverAttributeBo.setUpdateBy(0L);
        return driverAttributeBo;
    }


}
