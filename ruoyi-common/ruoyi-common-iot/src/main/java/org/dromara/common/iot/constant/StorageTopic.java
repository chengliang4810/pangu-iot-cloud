package org.dromara.common.iot.constant;

/**
 * 存储相关主题
 *
 * @author chengliang4810
 * @date 2023/07/17
 */
public interface StorageTopic {

    /**
     * 存储物模型属性
     */
    String STORAGE_DEVICE_PROPERTY_TOPIC_TPL = "storage/device/{}/property";

    /**
     * 获取存储物模型属性主题
     *
     * @param deviceId 设备唯一标识
     * @return {@link String}
     */
    static String getStorageDevicePropertyTopic(String deviceId) {
        return STORAGE_DEVICE_PROPERTY_TOPIC_TPL.replace("{}", deviceId);
    }

    /**
     * 获取存储物模型属性主题
     *
     * @param deviceId 设备唯一标识
     * @return {@link String}
     */
    static String getStorageDevicePropertyTopic(Long deviceId) {
        return STORAGE_DEVICE_PROPERTY_TOPIC_TPL.replace("{}", String.valueOf(deviceId));
    }

    /**
     * 存储物模型属性订阅主题
     */
    String  STORAGE_DEVICE_PROPERTY_SUB_TOPIC = "storage/device/+/property";

}
