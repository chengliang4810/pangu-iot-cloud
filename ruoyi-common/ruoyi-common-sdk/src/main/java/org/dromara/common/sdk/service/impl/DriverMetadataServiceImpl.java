/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.common.sdk.service.impl;

import io.github.pnoker.common.dto.DriverMetadataDTO;
import io.github.pnoker.common.enums.MetadataCommandTypeEnum;
import io.github.pnoker.common.model.*;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.driver.sdk.service.DriverMetadataService;
import io.github.pnoker.driver.sdk.service.DriverMetadataTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 驱动元数据相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverMetadataServiceImpl implements DriverMetadataService {

    @Resource
    private DriverMetadataTempService driverMetadataTempService;

    @Override
    public void profileMetadata(DriverMetadataDTO entityDTO) {
        Profile profile = JsonUtil.parseObject(entityDTO.getContent(), Profile.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert profile: {}", JsonUtil.toJsonString(profile));
            driverMetadataTempService.upsertProfile(profile);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete profile: {}", JsonUtil.toJsonString(profile));
            driverMetadataTempService.deleteProfile(profile.getId());
        }
    }

    @Override
    public void deviceMetadata(DriverMetadataDTO entityDTO) {
        Device device = JsonUtil.parseObject(entityDTO.getContent(), Device.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert device: {}", JsonUtil.toJsonString(device));
            driverMetadataTempService.upsertDevice(device);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete device: {}", JsonUtil.toJsonString(device));
            driverMetadataTempService.deleteDevice(device.getId());
        }
    }

    @Override
    public void pointMetadata(DriverMetadataDTO entityDTO) {
        Point point = JsonUtil.parseObject(entityDTO.getContent(), Point.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert point: {}", JsonUtil.toJsonString(point));
            driverMetadataTempService.upsertPoint(point);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete point: {}", JsonUtil.toJsonString(point));
            driverMetadataTempService.deletePoint(point.getProfileId(), point.getId());
        }
    }

    @Override
    public void driverInfoMetadata(DriverMetadataDTO entityDTO) {
        DriverAttributeConfig driverAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), DriverAttributeConfig.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
            driverMetadataTempService.upsertDriverInfo(driverAttributeConfig);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
            driverMetadataTempService.deleteDriverInfo(driverAttributeConfig.getDeviceId(), driverAttributeConfig.getDriverAttributeId());
        }
    }

    @Override
    public void pointInfoMetadata(DriverMetadataDTO entityDTO) {
        PointAttributeConfig pointAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), PointAttributeConfig.class);
        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Upsert point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
            driverMetadataTempService.upsertPointInfo(pointAttributeConfig);
        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
            log.info("Delete point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
            driverMetadataTempService.deletePointInfo(pointAttributeConfig.getPointId(), pointAttributeConfig.getId(), pointAttributeConfig.getPointAttributeId());
        }
    }
}
