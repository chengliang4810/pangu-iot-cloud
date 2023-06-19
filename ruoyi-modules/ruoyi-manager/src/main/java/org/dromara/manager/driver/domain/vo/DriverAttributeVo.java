package org.dromara.manager.driver.domain.vo;

import org.dromara.manager.driver.domain.DriverAttribute;
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
 * 驱动属性视图对象 iot_driver_attribute
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DriverAttribute.class)
public class DriverAttributeVo implements Serializable {

    @Serial
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
     * 属性名称
     */
    @ExcelProperty(value = "属性名称")
    private String attributeName;

    /**
     * 属性类型
     */
    @ExcelProperty(value = "属性类型")
    private String attributeType;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String displayName;

    /**
     * 默认值
     */
    @ExcelProperty(value = "默认值")
    private String defaultValue;

    /**
     * 必填
     */
    @ExcelProperty(value = "必填")
    private Integer required;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


}