package com.pangu.iot.manager.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 产品视图对象
 *
 * @author chengliang4810
 * @date 2022-12-30
 */
@Data
@ExcelIgnoreUnannotated
public class ProductVO {

    private static final long serialVersionUID = 1L;

    /**
     * 产品主键
     */
     @ExcelProperty(value = "产品主键")
     private String productId;

    /**
     * 产品分组ID
     */
     @ExcelProperty(value = "产品分组ID")
     private String groupId;

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
     @ExcelProperty(value = "厂家")
     private String manufacturer;

    /**
     * 型号
     */
     @ExcelProperty(value = "型号")
     private String model;

    /**
     * 备注
     */
     @ExcelProperty(value = "备注")
     private String remark;

    /**
     * Zabbix对应模板ID

     */
     @ExcelProperty(value = "Zabbix对应模板ID")
     private String zbxId;


}
