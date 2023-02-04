package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 产品视图对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@ExcelIgnoreUnannotated
public class ProductVO {

    private static final long serialVersionUID = 1L;

    /**
     * 产品主键
     */
    @ExcelProperty(value = "产品主键")
    private Long id;

    /**
     * 产品分组ID
     */
    @ExcelProperty(value = "产品分组ID")
    private Long groupId;

    /**
     * 产品编号
     */
    @ExcelProperty(value = "产品编号")
    private String code;

    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String name;

    /**
     * 产品类型
     */
    @ExcelProperty(value = "产品类型")
    private String type;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icon;

    /**
     * 厂家
     */
    @ExcelProperty(value = "厂家 ")
    private String manufacturer;

    /**
     * 型号
     */
    @ExcelProperty(value = "型号")
    private String model;

    /**
     * 设备数
     */
    @ExcelProperty(value = "设备数")
    private Integer deviceCount;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

}
