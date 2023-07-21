package org.dromara.common.iot.constant;

import cn.hutool.core.util.StrUtil;

/**
 * 物模型属性相关主题
 *
 * @author chengliang4810
 * @date 2023/07/10
 */
public interface DeviceAttributeTopic {

    /**
     * 物模型属性上报主题模板
     */
    String DEVICE_ATTRIBUTE_REPORT_TOPIC_TPL = "device/{}/property/report";

    /**
     * 物模型属性上报主题
     *
     * @param deviceId 设备唯一标识
     * @return {@link String}
     */
    static String getDeviceAttributeReportTopic(String deviceId) {
        return StrUtil.format(DEVICE_ATTRIBUTE_REPORT_TOPIC_TPL, deviceId);
    }

    /**
     * 物模型属性上报主题
     *
     * @param deviceId   设备唯一标识
     * @return {@link String}
     */
    static String getDeviceAttributeReportTopic(Long deviceId) {
        return StrUtil.format(DEVICE_ATTRIBUTE_REPORT_TOPIC_TPL, deviceId);
    }

    /**
     * 物模型属性上报订阅主题
     */
    String DEVICE_STATUS_SUB_TOPIC = "device/+/property/report";

}
