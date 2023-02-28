package com.pangu.iot.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 驱动属性配置信息视图对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class DriverInfoVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 驱动属性ID
     */
    @ExcelProperty(value = "驱动属性ID")
    private Long driverAttributeId;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    private String value;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;


}
