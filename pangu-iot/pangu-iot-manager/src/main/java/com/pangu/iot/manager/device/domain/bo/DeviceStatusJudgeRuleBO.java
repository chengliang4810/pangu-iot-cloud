package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import lombok.Data;
import net.dreamlu.mica.core.validation.DeleteGroup;
import net.dreamlu.mica.core.validation.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备状态判断规则
 * 设备 离线/上线 规则
 *
 * @author chengliang4810
 * @date 2023/02/02 09:45
 */

@Data
public class DeviceStatusJudgeRuleBO {


    /**
     *  deviceId / productId
     */
    @NotNull(groups = { AddGroup.class})
    private Long relationId;

    /**
     * 规则id
     * 自动生成，trigger name
     */
    @NotNull(groups = {UpdateGroup.class, DeleteGroup.class})
    private Long ruleId;

    private String ruleName;

    //#####################  下线规则

    /**
     * 设备属性ID
     */
    @NotNull(groups = {AddGroup.class})
    private Long attributeId;

    /**
     * 规则函数
     * nodata 或者 > < = 函数
     */
    @NotBlank(groups = {AddGroup.class})
    private String ruleFunction;

    /**
     * 规则条件
     * 时间 或者 特定值
     */
    @NotBlank(groups = {AddGroup.class})
    private String ruleCondition;

    /**
     * 单位
     * 单位 s m h 或空
     */
    @NotNull(groups = {AddGroup.class})
    private String unit;

    /**
     * 属性 Key
     */
    @NotBlank(groups = {AddGroup.class})
    private String attributeKey;


    //#####################  上线规则

    /**
     *  属性ID
     */
    @NotNull(groups = {AddGroup.class})
    private Long attributeIdRecovery;

    /**
     * 规则函数恢复
     * nodata 或者 > < = 函数
     */
    @NotBlank(groups = {AddGroup.class})
    private String ruleFunctionRecovery;

    /**
     * 规则条件恢复
     * 时间 或者 特定值
     */
    @NotBlank(groups = {AddGroup.class})
    private String ruleConditionRecovery;

    /**
     * 属性 Key
     */
    @NotBlank(groups = {AddGroup.class})
    private String attributeKeyRecovery;

    private String unitRecovery;
}
