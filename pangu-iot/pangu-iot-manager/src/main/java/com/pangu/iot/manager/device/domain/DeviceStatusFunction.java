package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备上下线规则对象 iot_device_status_function
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_status_function")
public class DeviceStatusFunction extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 下线属性ID
     */
    private Long attributeId;
    /**
     * 下线规则函数
     */
    private String ruleFunction;
    /**
     * 下线规则条件
     */
    private String ruleCondition;
    /**
     * 下线单位描述
     */
    private String unit;
    /**
     * 上线属性ID
     */
    private Long attributeIdRecovery;
    /**
     * 上线规则函数
     */
    private String ruleFunctionRecovery;
    /**
     * 上线规则条件
     */
    private String ruleConditionRecovery;
    /**
     * 上线单位描述
     */
    private String unitRecovery;
    /**
     * 状态
     */
    private Long status;
    /**
     * 备注
     */
    private String remark;

}
