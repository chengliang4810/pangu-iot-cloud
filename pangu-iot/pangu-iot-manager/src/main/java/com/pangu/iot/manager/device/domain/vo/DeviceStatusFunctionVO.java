package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 设备上下线规则视图对象
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceStatusFunctionVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 下线属性ID
     */
    @ExcelProperty(value = "下线属性ID")
    private Long attributeId;


    /**
     * 下线属性名称
     */
    private String attributeName;


    /**
     * 下线规则函数
     */
    @ExcelProperty(value = "下线规则函数")
    private String ruleFunction;

    /**
     * 下线规则条件
     */
    @ExcelProperty(value = "下线规则条件")
    private String ruleCondition;

    /**
     * 下线单位描述
     */
    @ExcelProperty(value = "下线单位描述")
    private String unit;

    /**
     * 上线属性ID
     */
    @ExcelProperty(value = "上线属性ID")
    private Long attributeIdRecovery;

    /**
     *  上线属性名字
     */
    @ExcelProperty(value = "上线属性名称")
    private String attributeNameRecovery;

    /**
     * 上线规则函数
     */
    @ExcelProperty(value = "上线规则函数")
    private String ruleFunctionRecovery;

    /**
     * 上线规则条件
     */
    @ExcelProperty(value = "上线规则条件")
    private String ruleConditionRecovery;

    /**
     * 上线单位描述
     */
    @ExcelProperty(value = "上线单位描述")
    private String unitRecovery;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Long status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
