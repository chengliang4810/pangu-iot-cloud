package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 设备上下线规则与设备关系视图对象
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceStatusFunctionRelationVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private Long ruleId;

    /**
     * 产品/设备ID
     */
    @ExcelProperty(value = "产品/设备ID")
    private Long relationId;

    /**
     * zbx下线触发器主键
     */
    @ExcelProperty(value = "zbx下线触发器主键")
    private Long zbxId;

    /**
     * zbx上线触发器主键
     */
    @ExcelProperty(value = "zbx上线触发器主键")
    private Long zbxIdRecovery;


}
