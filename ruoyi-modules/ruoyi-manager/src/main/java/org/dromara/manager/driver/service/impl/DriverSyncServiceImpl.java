package org.dromara.manager.driver.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.bo.DriverAttributeBo;
import org.dromara.manager.driver.domain.bo.DriverBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.domain.vo.DriverVo;
import org.dromara.manager.driver.service.DriverSyncService;
import org.dromara.manager.driver.service.IDriverAttributeService;
import org.dromara.manager.driver.service.IDriverAttributeValueService;
import org.dromara.manager.driver.service.IDriverService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverSyncServiceImpl implements DriverSyncService {

    private final IDriverService driverService;
    private final IDriverAttributeService driverAttributeService;
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


        DriverMetadata driverMetadata = new DriverMetadata();

        DriverSyncDownDTO driverSyncDownDTO = new DriverSyncDownDTO(JsonUtils.toJsonString(driverMetadata));

        EmqxUtil.getClient().publish(DriverTopic.getDriverRegisterBackTopic(entityDTO.getDriverUniqueKey()), JsonUtils.toJsonString(driverSyncDownDTO));
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

        if (driver == null){
            log.debug("Driver does not registered, adding {} ", driverDTO);
            DriverBo driverBo = buildDriverBo(driverDTO);
            driverService.insertByBo(driverBo);
            return driverService.queryById(driverBo.getId());
        }

        log.debug("Driver already registered, updating {} ", driverDTO);
        driverService.updateByBo(buildDriverBo(driverDTO).setId(driver.getId()));
        return driverService.queryById(driver.getId());
    }

    private DriverBo buildDriverBo(DriverDTO driverDTO){
        DriverBo driverBo = new DriverBo();
        driverBo.setCode(driverDTO.getDriverCode());
        driverBo.setDisplayName(driverDTO.getDriverName());
        driverBo.setRemark(driverDTO.getRemark());
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
                if (count == 0){
                    log.debug("Driver attribute is redundant, deleting: {}", oldDriverAttributeMap.get(name));
                    driverAttributeService.deleteWithValidByIds(Collections.singletonList(oldDriverAttributeMap.get(name).getId()), false);
                    continue;
                }
                log.warn("The driver attribute(" + name + ") used by driver attribute config and cannot be deleted");
            }
        }
    }

    private DriverAttributeBo buildDriverAttributeBo(DriverAttribute driverAttribute){
        DriverAttributeBo driverAttributeBo = new DriverAttributeBo();
        driverAttributeBo.setId(driverAttribute.getId());
        driverAttributeBo.setDriverId(driverAttribute.getDriverId());
        driverAttributeBo.setAttributeName(driverAttribute.getAttributeName());
        driverAttributeBo.setAttributeType(driverAttributeBo.getAttributeType());
        driverAttributeBo.setDisplayName(driverAttribute.getDisplayName());
        driverAttributeBo.setDefaultValue(driverAttribute.getDefaultValue());
        driverAttributeBo.setRequired(driverAttribute.getRequired());
        driverAttributeBo.setRemark(driverAttributeBo.getRemark());
        return driverAttributeBo;
    }


}
