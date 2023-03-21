package com.pangu.iot.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 驱动属性视图对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class DriverAttributeVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 驱动ID
     */
    @ExcelProperty(value = "驱动ID")
    private Long driverId;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String displayName;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 类型
     */
    @ExcelProperty(value = "类型")
    private String type;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    private String value;

    /**
     * 默认值
     */
    @ExcelProperty(value = "默认值")
    private String defaultValue;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;


}
