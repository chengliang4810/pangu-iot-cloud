package org.dromara.common.iot.constant;

/**
 * 设备相关主题
 *
 * @author chengliang4810
 * @date 2023/06/25
 */
public interface DeviceTopic {

    /**
     * 驱动程序注册主题模板
     */
    String DEVICE_STATUS_TOPIC_TPL = "device/{}/status";

    /**
     * 获取设备状态主题
     *
     * @param deviceId 设备唯一标识
     * @return {@link String}
     */
    static String getDeviceStatusTopic(String deviceId) {
        return DEVICE_STATUS_TOPIC_TPL.replace("{}", deviceId);
    }

    /**
     * 获取设备状态主题
     *
     * @param deviceId 设备唯一标识
     * @return {@link String}
     */
    static String getDeviceStatusTopic(Long deviceId) {
        return DEVICE_STATUS_TOPIC_TPL.replace("{}", String.valueOf(deviceId));
    }

    /**
     * 设备状态订阅主题
     */
    String DEVICE_STATUS_SUB_TOPIC = "driver/+/status";

}
