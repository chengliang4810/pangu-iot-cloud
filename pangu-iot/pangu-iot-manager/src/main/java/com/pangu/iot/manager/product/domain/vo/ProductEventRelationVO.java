package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 告警规则关系视图对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@ExcelIgnoreUnannotated
public class ProductEventRelationVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private Long eventRuleId;

    /**
     * 关联产品或设备ID
     */
    @ExcelProperty(value = "关联产品或设备ID")
    private Long relationId;

    /**
     * trigger id
     */
    @ExcelProperty(value = "trigger id")
    private String zbxId;

    /**
     * 是否来自产品
     */
    @ExcelProperty(value = "是否来自产品")
    private Long inherit;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Boolean status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
