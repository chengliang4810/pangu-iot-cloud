package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 告警规则达式对象 iot_product_event_expression
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_event_expression")
public class ProductEventExpression extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 规则ID
     */
    private Long ruleId;
    /**
     * 函数
     */
    @TableField("`function`")
    private String function;
    /**
     * 作用域
     */
    private String scope;
    /**
     * 条件
     */
    @TableField("`condition`")
    private String condition;
    /**
     * 值
     */
    private String value;
    /**
     * 单位
     */
    private String unit;
    /**
     * 产品/设备ID
     */
    private Long relationId;
    /**
     * 属性ID
     */
    private Long productAttributeId;
    /**
     * 属性key
     */
    private String productAttributeKey;
    /**
     * 属性类型 属性 事件
     */
    private String productAttributeType;
    /**
     * 取值周期 时间 周期
     */
    private String period;
    /**
     * 属性值类型
     */
    private String attributeValueType;
    /**
     * 备注
     */
    private String remark;

}
