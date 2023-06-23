package org.dromara.common.sdk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.sdk.service.DriverMetadataService;
import org.springframework.stereotype.Service;

/**
 * 驱动元数据相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverMetadataServiceImpl implements DriverMetadataService {

//    @Resource
//    private DriverMetadataTempService driverMetadataTempService;
//
//    @Override
//    public void profileMetadata(DriverMetadataDTO entityDTO) {
//        Profile profile = JsonUtil.parseObject(entityDTO.getContent(), Profile.class);
//        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Upsert profile: {}", JsonUtil.toJsonString(profile));
//            driverMetadataTempService.upsertProfile(profile);
//        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Delete profile: {}", JsonUtil.toJsonString(profile));
//            driverMetadataTempService.deleteProfile(profile.getId());
//        }
//    }
//
//    @Override
//    public void deviceMetadata(DriverMetadataDTO entityDTO) {
//        Device device = JsonUtil.parseObject(entityDTO.getContent(), Device.class);
//        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Upsert device: {}", JsonUtil.toJsonString(device));
//            driverMetadataTempService.upsertDevice(device);
//        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Delete device: {}", JsonUtil.toJsonString(device));
//            driverMetadataTempService.deleteDevice(device.getId());
//        }
//    }
//
//    @Override
//    public void pointMetadata(DriverMetadataDTO entityDTO) {
//        Point point = JsonUtil.parseObject(entityDTO.getContent(), Point.class);
//        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Upsert point: {}", JsonUtil.toJsonString(point));
//            driverMetadataTempService.upsertPoint(point);
//        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Delete point: {}", JsonUtil.toJsonString(point));
//            driverMetadataTempService.deletePoint(point.getProfileId(), point.getId());
//        }
//    }
//
//    @Override
//    public void driverInfoMetadata(DriverMetadataDTO entityDTO) {
//        DriverAttributeConfig driverAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), DriverAttributeConfig.class);
//        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Upsert driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
//            driverMetadataTempService.upsertDriverInfo(driverAttributeConfig);
//        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Delete driver attribute config: {}", JsonUtil.toJsonString(driverAttributeConfig));
//            driverMetadataTempService.deleteDriverInfo(driverAttributeConfig.getDeviceId(), driverAttributeConfig.getDriverAttributeId());
//        }
//    }
//
//    @Override
//    public void pointInfoMetadata(DriverMetadataDTO entityDTO) {
//        PointAttributeConfig pointAttributeConfig = JsonUtil.parseObject(entityDTO.getContent(), PointAttributeConfig.class);
//        if (MetadataCommandTypeEnum.ADD.equals(entityDTO.getMetadataCommandType()) || MetadataCommandTypeEnum.UPDATE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Upsert point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
//            driverMetadataTempService.upsertPointInfo(pointAttributeConfig);
//        } else if (MetadataCommandTypeEnum.DELETE.equals(entityDTO.getMetadataCommandType())) {
//            log.info("Delete point attribute config: {}", JsonUtil.toJsonString(pointAttributeConfig));
//            driverMetadataTempService.deletePointInfo(pointAttributeConfig.getPointId(), pointAttributeConfig.getId(), pointAttributeConfig.getPointAttributeId());
//        }
//    }
}
