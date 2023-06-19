package org.dromara.manager.driver.domain.vo;

import org.dromara.manager.driver.domain.DriverAttributeValue;
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
 * 驱动属性值视图对象 iot_driver_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DriverAttributeValue.class)
public class DriverAttributeValueVo implements Serializable {

    @Serial
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
     * 网关设备ID
     */
    @ExcelProperty(value = "网关设备ID")
    private Long gatewayDeviceId;

    /**
     * 属性类型
     */
    @ExcelProperty(value = "属性类型")
    private String value;


}
