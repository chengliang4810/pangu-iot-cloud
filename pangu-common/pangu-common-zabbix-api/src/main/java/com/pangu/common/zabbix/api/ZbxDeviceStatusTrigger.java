package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

/**
 * 设备离线 在线触发器，判断设备 在线，离线 状态
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxDeviceStatusTrigger extends BaseApi {


    /**
     * 创建 设备 在线，离线 触发器
     *
     * @return String
     */
    @Post
    @JsonPath("/trigger/device.status.trigger")
    String createDeviceStatusTrigger(@ParamName("ruleId") String ruleId,
                                     @ParamName("deviceId") String deviceId,
                                     @ParamName("itemKey") String itemKey,
                                     @ParamName("ruleCondition") String ruleCondition,
                                     @ParamName("ruleFunction") String ruleFunction,
                                     @ParamName("itemKeyRecovery") String itemKeyRecovery,
                                     @ParamName("ruleConditionRecovery") String ruleConditionRecovery,
                                     @ParamName("ruleFunctionRecovery") String ruleFunctionRecovery);


    /**
     * 修改 设备 在线，离线 触发器
     *
     * @return String
     */
    @Post
    @JsonPath("/trigger/device.status.trigger.update")
    String updateDeviceStatusTrigger(@ParamName("triggerId") String triggerId,
                                     @ParamName("ruleId") String ruleId,
                                     @ParamName("deviceId") String deviceId,
                                     @ParamName("itemKey") String itemKey,
                                     @ParamName("ruleCondition") String ruleCondition,
                                     @ParamName("ruleFunction") String ruleFunction,
                                     @ParamName("itemKeyRecovery") String itemKeyRecovery,
                                     @ParamName("ruleConditionRecovery") String ruleConditionRecovery,
                                     @ParamName("ruleFunctionRecovery") String ruleFunctionRecovery,
                                     @ParamName("recoveryTriggerId") String recoveryTriggerId);
}
