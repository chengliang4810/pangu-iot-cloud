package com.pangu.common.zabbix.service;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxDeviceStatusTrigger;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusTriggerService {

    private final ZbxDeviceStatusTrigger zbxDeviceStatusTrigger;

    /**
     * 创建设备状态触发
     *
     * @param ruleId                规则id
     * @param relationId            关系id
     * @param attributeKey          属性关键
     * @param ruleCondition         规则条件
     * @param ruleFunction          规则函数
     * @param attributeKeyRecovery  属性关键复苏
     * @param ruleConditionRecovery 规则条件恢复
     * @param ruleFunctionRecovery  规则函数恢复
     * @return {@link String[]}
     */
    public String[] createDeviceStatusTrigger(String ruleId, String relationId, String attributeKey, String ruleCondition, String ruleFunction, String attributeKeyRecovery, String ruleConditionRecovery, String ruleFunctionRecovery) {
        String response = zbxDeviceStatusTrigger.createDeviceStatusTrigger(ruleId, relationId, attributeKey, ruleCondition, ruleFunction, attributeKeyRecovery, ruleConditionRecovery, ruleFunctionRecovery);
        return getTriggerId(response);
    }

    public void updateDeviceStatusTrigger(String zbxId, String ruleId, String relationId, String attributeKey, String ruleCondition, String ruleFunction, String attributeKeyRecovery, String ruleConditionRecovery, String ruleFunctionRecovery, String zbxIdRecovery) {
        zbxDeviceStatusTrigger.updateDeviceStatusTrigger(zbxId, ruleId, relationId, attributeKey, ruleCondition, ruleFunction, attributeKeyRecovery, ruleConditionRecovery, ruleFunctionRecovery, zbxIdRecovery);
    }

    private String[] getTriggerId(String responseStr) {
        TriggerIds ids = JsonUtils.parseObject(responseStr, TriggerIds.class);
        if (null == ids || ids.getTriggerids().length != 2) {
            throw new ServiceException("创建设备状态触发器失败");
        }
        return ids.getTriggerids();
    }



    @Data
    static class TriggerIds {
        String[] triggerids;
    }

}
