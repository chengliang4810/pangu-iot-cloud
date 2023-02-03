package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 告警规则达式视图对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@ExcelIgnoreUnannotated
public class ProductEventExpressionVO {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private Long ruleId;

    /**
     * 函数
     */
    @ExcelProperty(value = "函数")
    private String function;

    /**
     * 作用域
     */
    @ExcelProperty(value = "作用域")
    private String scope;

    /**
     * 表达式
     */
    @ExcelProperty(value = "表达式")
    private String condition;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    private String value;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 产品/设备ID
     */
    @ExcelProperty(value = "产品/设备ID")
    private Long relationId;

    /**
     * 属性ID
     */
    @ExcelProperty(value = "属性ID")
    private Long productAttributeId;

    /**
     * 属性key
     */
    @ExcelProperty(value = "属性key")
    private String productAttributeKey;

    /**
     * 属性类型 属性 事件
     */
    @ExcelProperty(value = "属性类型 属性 事件")
    private String productAttributeType;

    /**
     * 取值周期 时间 周期
     */
    @ExcelProperty(value = "取值周期 时间 周期")
    private String period;

    /**
     * 属性值类型
     */
    @ExcelProperty(value = "属性值类型")
    private String attributeValueType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
