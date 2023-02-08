package com.pangu.iot.manager.device.service;

import com.pangu.iot.manager.device.domain.bo.DeviceEventRuleBO;

/**
 * 设备告警规则Service接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
public interface IDeviceEventRuleService {

    /**
     * 创建设备告警规则
     *
     * @param bo 设备告警规则信息
     * @return {@link Boolean}
     */
    Boolean createDeviceEventRule(DeviceEventRuleBO bo);
}
