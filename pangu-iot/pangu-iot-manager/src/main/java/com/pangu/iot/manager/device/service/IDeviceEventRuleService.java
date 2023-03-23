package com.pangu.iot.manager.device.service;

import com.pangu.iot.manager.device.domain.bo.DeviceEventRuleBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAlarmRuleVO;

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

    /**
     * 通过id获取设备告警规则详情
     *
     * @param id id
     * @return {@link DeviceAlarmRuleVO}
     */
    DeviceAlarmRuleVO getById(Long id);

    /**
     * 更新设备告警事件
     *
     * @param bo 薄
     * @return {@link Boolean}
     */
    Boolean updateDeviceEventRuleByBo(DeviceEventRuleBO bo);

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    Boolean deleteByProductId(Long productId);
}
