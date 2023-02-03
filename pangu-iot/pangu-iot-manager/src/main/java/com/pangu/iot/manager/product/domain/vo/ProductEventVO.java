package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 告警规则视图对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@ExcelIgnoreUnannotated
public class ProductEventVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 告警规则名称
     */
    @ExcelProperty(value = "告警规则名称")
    private String name;

    /**
     * 告警等级
     */
    @ExcelProperty(value = "告警等级")
    private Long level;

    /**
     * and 或者 or
     */
    @ExcelProperty(value = "and 或者 or")
    private String expLogic;

    /**
     * 0 否 1 是
     */
    @ExcelProperty(value = "0 否 1 是")
    private Long notify;

    /**
     * 0 告警 1场景联动
     */
    @ExcelProperty(value = "0 告警 1场景联动")
    private Long classify;

    /**
     * 任务ID
     */
    @ExcelProperty(value = "任务ID")
    private Long taskId;

    /**
     * 触发类型 0-条件触发 1-定时触发
     */
    @ExcelProperty(value = "触发类型 0-条件触发 1-定时触发")
    private Long triggerType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
