package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 告警规则达式业务对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductEventExpressionBO extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规则ID
     */
    @NotNull(message = "规则ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long ruleId;

    /**
     * 函数
     */
    @NotBlank(message = "函数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String function;

    /**
     * 作用域
     */
    @NotBlank(message = "作用域不能为空", groups = { AddGroup.class, EditGroup.class })
    private String scope;

    /**
     * 表达式
     */
    @NotBlank(message = "表达式不能为空", groups = { AddGroup.class, EditGroup.class })
    private String condition;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 单位
     */
    @NotBlank(message = "单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unit;

    /**
     * 产品/设备ID
     */
    @NotNull(message = "产品/设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * 属性ID
     */
    @NotNull(message = "属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productAttributeId;

    /**
     * 属性key
     */
    @NotBlank(message = "属性key不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productAttributeKey;

    /**
     * 属性类型 属性 事件
     */
    @NotBlank(message = "属性类型 属性 事件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String productAttributeType;

    /**
     * 取值周期 时间 周期
     */
    @NotBlank(message = "取值周期 时间 周期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String period;

    /**
     * 属性值类型
     */
    @NotBlank(message = "属性值类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attributeValueType;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
