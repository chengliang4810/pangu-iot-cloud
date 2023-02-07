package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 告警规则与功能关系视图对象
 *
 * @author chengliang4810
 * @date 2023-02-07
 */
@Data
@ExcelIgnoreUnannotated
public class ProductEventServiceVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 告警规则ID
     */
    @ExcelProperty(value = "告警规则ID")
    private Long eventRuleId;

    /**
     * 功能ID
     */
    @ExcelProperty(value = "功能ID")
    private Long serviceId;

    /**
     * 产品、设备ID
     */
    @ExcelProperty(value = "产品、设备ID")
    private Long relationId;

    /**
     * 执行目标设备ID
     */
    @ExcelProperty(value = "执行目标设备ID")
    private Long executeDeviceId;


}
