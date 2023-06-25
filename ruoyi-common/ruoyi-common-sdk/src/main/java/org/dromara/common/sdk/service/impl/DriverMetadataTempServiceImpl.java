package org.dromara.common.sdk.service.impl;

import cn.hutool.setting.profile.Profile;
import com.graphbuilder.curve.Point;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.entity.driver.DriverAttributeConfig;
import org.dromara.common.iot.entity.driver.PointAttributeConfig;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.service.DriverMetadataTempService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Driver Metadata Service Implements
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverMetadataTempServiceImpl implements DriverMetadataTempService {

    @Resource
    private DriverContext driverContext;

    @Override
    public void upsertProfile(Profile profile) {
        // Add profile point to context
        // driverContext.getDriverMetadata().getProfilePointMap().computeIfAbsent(profile.getId(), k -> new ConcurrentHashMap<>(16));
    }

    @Override
    public void deleteProfile(String id) {
        driverContext.getDriverMetadata().getProfilePointMap().entrySet().removeIf(next -> next.getKey().equals(id));
    }

    @Override
    public void upsertDevice(Device device) {
        // Add device to context
        // driverContext.getDriverMetadata().getDeviceMap().put(device.getId(), device);
        // Add device driver attribute config to context
        // driverContext.getDriverMetadata().getDriverInfoMap().computeIfAbsent(device.getId(), k -> new ConcurrentHashMap<>(16));
        // Add device point attribute config to context
        // driverContext.getDriverMetadata().getPointInfoMap().computeIfAbsent(device.getId(), k -> new ConcurrentHashMap<>(16));
    }

    @Override
    public void deleteDevice(String id) {
        driverContext.getDriverMetadata().getDeviceMap().entrySet().removeIf(next -> next.getKey().equals(id));
        driverContext.getDriverMetadata().getDriverInfoMap().entrySet().removeIf(next -> next.getKey().equals(id));
        driverContext.getDriverMetadata().getPointInfoMap().entrySet().removeIf(next -> next.getKey().equals(id));
    }

    @Override
    public void upsertPoint(Point point) {
        // Upsert point to profile point map context
        // driverContext.getDriverMetadata().getProfilePointMap().computeIfAbsent(point.getProfileId(), k -> new ConcurrentHashMap<>(16)).put(point.getId(), point);
    }

    @Override
    public void deletePoint(String profileId, String pointId) {
        // Delete point from profile point map context
//        driverContext.getDriverMetadata().getProfilePointMap().computeIfPresent(profileId, (k, v) -> {
//            v.entrySet().removeIf(next -> next.getKey().equals(pointId));
//            return v;
//        });
    }

    @Override
    public void upsertDriverInfo(DriverAttributeConfig driverAttributeConfig) {
//        DriverAttribute attribute = driverContext.getDriverMetadata().getDriverAttributeMap().get(driverAttributeConfig.getDriverAttributeId());
//        if (ObjectUtil.isNotNull(attribute)) {
//            // Add driver attribute config to driver attribute config map context
//            driverContext.getDriverMetadata().getDriverInfoMap().computeIfAbsent(driverAttributeConfig.getDeviceId(), k -> new ConcurrentHashMap<>(16))
//                    .put(attribute.getAttributeName(), new AttributeInfo(driverAttributeConfig.getConfigValue(), attribute.getAttributeTypeFlag()));
//        }
    }

    @Override
    public void deleteDriverInfo(String deviceId, String attributeId) {
//        DriverAttribute attribute = driverContext.getDriverMetadata().getDriverAttributeMap().get(attributeId);
//        if (ObjectUtil.isNotNull(attribute)) {
//            // Delete driver attribute config from driver attribute config map context
//            driverContext.getDriverMetadata().getDriverInfoMap().computeIfPresent(deviceId, (k, v) -> {
//                v.entrySet().removeIf(next -> next.getKey().equals(attribute.getAttributeName()));
//                return v;
//            });
//
//            // If the driver attribute is null, delete the driver attribute config from the driver attribute config map context
//            driverContext.getDriverMetadata().getDriverInfoMap().entrySet().removeIf(next -> next.getValue().size() < 1);
//        }
    }

    @Override
    public void upsertPointInfo(PointAttributeConfig pointAttributeConfig) {
//        PointAttribute attribute = driverContext.getDriverMetadata().getPointAttributeMap().get(pointAttributeConfig.getPointAttributeId());
//        if (ObjectUtil.isNotNull(attribute)) {
//            // Add the point attribute config to the device point attribute config map context
//            driverContext.getDriverMetadata().getPointInfoMap().computeIfAbsent(pointAttributeConfig.getDeviceId(), k -> new ConcurrentHashMap<>(16))
//                    .computeIfAbsent(pointAttributeConfig.getPointId(), k -> new ConcurrentHashMap<>(16))
//                    .put(attribute.getAttributeName(), new AttributeInfo(pointAttributeConfig.getConfigValue(), attribute.getAttributeTypeFlag()));
//        }
    }

    @Override
    public void deletePointInfo(String deviceId, String pointId, String attributeId) {
        //        PointAttribute attribute = driverContext.getDriverMetadata().getPointAttributeMap().get(attributeId);
//        if (ObjectUtil.isNotNull(attribute)) {
//            // Delete the point attribute config from the device info map context
//            driverContext.getDriverMetadata().getPointInfoMap().computeIfPresent(deviceId, (key1, value1) -> {
//                value1.computeIfPresent(pointId, (key2, value2) -> {
//                    value2.entrySet().removeIf(next -> next.getKey().equals(attribute.getAttributeName()));
//                    return value2;
//                });
//                return value1;
//            });
//
//            // If the point attribute is null, delete the point attribute config from the point attribute config map context
//            driverContext.getDriverMetadata().getPointInfoMap().computeIfPresent(deviceId, (key, value) -> {
//                value.entrySet().removeIf(next -> next.getValue().size() < 1);
//                return value;
//            });
//        }
    }

}
