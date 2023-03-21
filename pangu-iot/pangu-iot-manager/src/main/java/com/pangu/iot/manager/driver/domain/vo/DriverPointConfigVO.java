package com.pangu.iot.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;


/**
 * 协议驱动视图对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class DriverPointConfigVO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议名称
     */
    @ExcelProperty(value = "协议名称")
    private String name;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String displayName;


    /**
     * 启用|禁用
     */
    @ExcelProperty(value = "启用|禁用")
    private Boolean status;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;


    /**
     * 属性列表
     */
    private  List<PointAttributeVO> attributeList;


}
