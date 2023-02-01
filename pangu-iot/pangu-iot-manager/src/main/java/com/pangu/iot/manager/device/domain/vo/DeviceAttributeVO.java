package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 设备属性视图对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceAttributeVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 产品ID
     */
    @ExcelProperty(value = "产品ID")
    private Long productId;

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号")
    private Long deviceId;

    /**
     * 属性名称
     */
    @ExcelProperty(value = "属性名称")
    private String name;

    /**
     * 属性值
     */
    private Object value;

    /**
     * 属性唯一Key

     */
    @ExcelProperty(value = "属性唯一Key")
    private String key;

    /**
     * 值类型
     */
    @ExcelProperty(value = "值类型")
    private String valueType;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String source;

    /**
     * 单位描述
     */
    @ExcelProperty(value = "单位描述")
    private String unit;

    /**
     * 主条目id

     */
    @ExcelProperty(value = "主条目id")
    private String masterItemId;

    /**
     *  依赖属性 id， 当 source为18时不为空

     */
    @ExcelProperty(value = " 依赖属性 id， 当 source为18时不为空")
    private Long dependencyAttrId;

    /**
     * zabbix ItemId
     */
    @ExcelProperty(value = "zabbix ItemId")
    private String zbxId;

    /**
     * 继承的ID
     */
    @ExcelProperty(value = "继承的ID")
    private String templateId;

    /**
     * zabbix 值映射ID
     */
    @ExcelProperty(value = "zabbix 值映射ID")
    private String valueMapId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
