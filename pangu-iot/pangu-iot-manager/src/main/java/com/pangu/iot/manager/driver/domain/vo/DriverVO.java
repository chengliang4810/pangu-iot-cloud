package com.pangu.iot.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 协议驱动视图对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@ExcelIgnoreUnannotated
public class DriverVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 协议名称
     */
    @ExcelProperty(value = "协议名称")
    private String name;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String displayName;

    /**
     * 协议服务名称
     */
    @ExcelProperty(value = "协议服务名称")
    private String serviceName;

    /**
     * 主机IP
     */
    @ExcelProperty(value = "主机IP")
    private String host;

    /**
     * 端口
     */
    @ExcelProperty(value = "端口")
    private Long port;

    /**
     * 启用|禁用
     */
    @ExcelProperty(value = "启用|禁用")
    private Long enable;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;


}
