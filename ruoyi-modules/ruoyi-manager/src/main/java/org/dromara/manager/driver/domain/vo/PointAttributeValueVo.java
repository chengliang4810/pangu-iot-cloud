package org.dromara.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.driver.domain.PointAttributeValue;

import java.io.Serial;
import java.io.Serializable;


/**
 * 驱动属性值视图对象 iot_point_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = PointAttributeValue.class)
public class PointAttributeValueVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 点位属性ID
     */
    @ExcelProperty(value = "点位属性ID")
    private Long pointAttributeId;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;

    /**
     * 设备属性ID
     */
    @ExcelProperty(value = "设备属性ID")
    private Long deviceAttributeId;

    /**
     * 属性值
     */
    @ExcelProperty(value = "属性值")
    private String value;


}
