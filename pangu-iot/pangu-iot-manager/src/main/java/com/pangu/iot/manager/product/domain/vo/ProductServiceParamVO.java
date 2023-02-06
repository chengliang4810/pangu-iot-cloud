package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 产品功能参数视图对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@ExcelIgnoreUnannotated
public class ProductServiceParamVO {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 服务ID
     */
    @ExcelProperty(value = "服务ID")
    private Long serviceId;

    /**
     * 参数标识
     */
    @ExcelProperty(value = "参数标识")
    private String key;

    /**
     * 参数名
     */
    @ExcelProperty(value = "参数名")
    private String name;

    /**
     * 参数值
     */
    @ExcelProperty(value = "参数值")
    private String value;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 设备IDremark
     */
    @ExcelProperty(value = "设备IDremark")
    private Long deviceId;


}
