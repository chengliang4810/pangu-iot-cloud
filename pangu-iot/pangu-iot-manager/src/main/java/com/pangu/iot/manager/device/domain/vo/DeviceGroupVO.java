package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 设备分组视图对象
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceGroupVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    private String name;

    /**
     * zabbix ItemId
     */
    @ExcelProperty(value = "zabbix ItemId")
    private String zbxId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
