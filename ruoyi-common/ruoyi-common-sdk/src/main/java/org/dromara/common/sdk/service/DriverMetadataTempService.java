package org.dromara.common.sdk.service;

import cn.hutool.setting.profile.Profile;
import com.graphbuilder.curve.Point;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.entity.driver.DriverAttributeConfig;
import org.dromara.common.iot.entity.driver.PointAttributeConfig;

/**
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverMetadataTempService {

    /**
     * 向 DeviceDriver 中添加模板
     *
     * @param profile Profile
     */
    void upsertProfile(Profile profile);

    /**
     * 删除 DeviceDriver 中模板
     *
     * @param id ID
     */
    void deleteProfile(String id);

    /**
     * 向 DeviceDriver 中添加设备
     *
     * @param device Device
     */
    void upsertDevice(Device device);

    /**
     * 删除 DeviceDriver 中设备
     *
     * @param id ID
     */
    void deleteDevice(String id);

    /**
     * 向 DeviceDriver 中添加位号
     *
     * @param point Point
     */
    void upsertPoint(Point point);

    /**
     * 删除 DeviceDriver 中位号
     *
     * @param profileId 模板ID
     * @param id        ID
     */
    void deletePoint(String profileId, String id);

    /**
     * 向 DeviceDriver 中添加驱动配置信息
     *
     * @param driverAttributeConfig DriverInfo
     */
    void upsertDriverInfo(DriverAttributeConfig driverAttributeConfig);

    /**
     * 删除 DeviceDriver 中添加驱动配置信息
     *
     * @param deviceId    设备ID
     * @param attributeId Attribute ID
     */
    void deleteDriverInfo(String deviceId, String attributeId);

    /**
     * 向 DeviceDriver 中添加位号配置信息
     *
     * @param pointAttributeConfig PointInfo
     */
    void upsertPointInfo(PointAttributeConfig pointAttributeConfig);

    /**
     * 删除 DeviceDriver 中添加位号配置信息
     *
     * @param deviceId    设备ID
     * @param pointId     Point ID
     * @param attributeId Attribute ID
     */
    void deletePointInfo(String deviceId, String pointId, String attributeId);
}
