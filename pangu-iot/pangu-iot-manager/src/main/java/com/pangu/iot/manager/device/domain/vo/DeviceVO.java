package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;



/**
 * 设备视图对象
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceVO {

    private static final long serialVersionUID = 1L;

    /**
     * 设备主键
     */
    @ExcelProperty(value = "设备主键")
    private Long id;

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号")
    private String code;

    /**
     * 设备分组ID
     */
    @ExcelProperty(value = "设备分组ID")
    private Long groupId;

    /**
     * 产品ID
     */
    @ExcelProperty(value = "产品ID")
    private Long productId;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String name;

    /**
     * 设备类型
     */
    @ExcelProperty(value = "设备类型")
    private String type;

    /**
     * 设备地址
     */
    @ExcelProperty(value = "设备地址")
    private String address;

    /**
     * 地址坐标
     */
    @ExcelProperty(value = "地址坐标")
    private String position;

    /**
     * 最近在线时间
     */
    @ExcelProperty(value = "最近在线时间")
    private Date latestOnline;

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
