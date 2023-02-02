package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备上下线规则业务对象
 *
 * @author chengliang4810
 * @date 2023-02-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceStatusFunctionBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 下线属性ID
     */
    @NotNull(message = "下线属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long attributeId;

    /**
     * 下线规则函数
     */
    @NotBlank(message = "下线规则函数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleFunction;

    /**
     * 下线规则条件
     */
    @NotBlank(message = "下线规则条件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleCondition;

    /**
     * 下线单位描述
     */
    @NotBlank(message = "下线单位描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unit;

    /**
     * 上线属性ID
     */
    @NotNull(message = "上线属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long attributeIdRecovery;

    /**
     * 上线规则函数
     */
    @NotBlank(message = "上线规则函数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleFunctionRecovery;

    /**
     * 上线规则条件
     */
    @NotBlank(message = "上线规则条件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ruleConditionRecovery;

    /**
     * 上线单位描述
     */
    @NotBlank(message = "上线单位描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unitRecovery;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
