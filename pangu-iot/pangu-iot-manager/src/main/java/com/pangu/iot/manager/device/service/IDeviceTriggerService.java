package com.pangu.iot.manager.device.service;

import com.pangu.iot.manager.device.domain.bo.DeviceStatusJudgeRuleBO;

/**
 * 设备触发器Service接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
public interface IDeviceTriggerService {


    /**
     * 创建设备状态判断触发器
     *
     * @param bo 规则
     * @return int
     */
    int createDeviceStatusJudgeTrigger(DeviceStatusJudgeRuleBO bo);
}
