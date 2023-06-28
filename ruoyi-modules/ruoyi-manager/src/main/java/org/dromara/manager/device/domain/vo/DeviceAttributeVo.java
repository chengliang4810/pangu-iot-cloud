package org.dromara.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.manager.device.domain.DeviceAttribute;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 设备属性视图对象 iot_device_attribute
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DeviceAttribute.class)
public class DeviceAttributeVo implements Serializable {

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
     * 属性名称
     */
    @ExcelProperty(value = "属性名称")
    private String attributeName;

    /**
     * 标识符
     */
    @ExcelProperty(value = "标识符")
    private String identifier;

    /**
     * 属性类型
     */
    @ExcelProperty(value = "属性类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_attribute_type")
    private String attributeType;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_units")
    private String unit;

    /**
     * 数据预处理代码
     */
    private String pretreatmentScript;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


}
