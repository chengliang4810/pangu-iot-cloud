package org.dromara.manager.device.domain.vo;

import org.dromara.manager.device.domain.DeviceFunction;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 设备功能视图对象 iot_device_function
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DeviceFunction.class)
public class DeviceFunctionVo implements Serializable {

    @Serial
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
     * 驱动ID
     */
    @ExcelProperty(value = "驱动ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_device_type")
    private Long driverId;

    /**
     * 设备属性
     */
    @ExcelProperty(value = "设备属性")
    private Long functionStatusAttribute;

    /**
     * 功能名称
     */
    @ExcelProperty(value = "功能名称")
    private String functionName;

    /**
     * 标识符
     */
    @ExcelProperty(value = "标识符")
    private String identifier;

    /**
     * 数据类型
     */
    @ExcelProperty(value = "数据类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_attribute_type")
    private String dataType;

    /**
     * 执行方式
     */
    @ExcelProperty(value = "执行方式", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_normal_disable")
    private Long async;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


}
