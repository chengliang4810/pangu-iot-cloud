package org.dromara.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.manager.product.domain.Product;

import java.io.Serial;
import java.io.Serializable;



/**
 * 产品视图对象 iot_product
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Product.class)
public class ProductVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 产品主键
     */
    @ExcelProperty(value = "产品主键")
    private Long id;

    /**
     * 产品分组ID
     */
    @ExcelProperty(value = "产品分组ID")
    private Long groupId;

    /**
     * 驱动ID
     */
    @ExcelProperty(value = "驱动ID")
    private Long driverId;

    /**
     * 产品名称
     */
    @ExcelProperty(value = "产品名称")
    private String name;

    /**
     * 产品类型
     */
    @ExcelProperty(value = "产品类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_device_type")
    private Integer type;

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
     * 设备总数
     */
    @ExcelProperty(value = "设备总数")
    private Long deviceCount;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


}
