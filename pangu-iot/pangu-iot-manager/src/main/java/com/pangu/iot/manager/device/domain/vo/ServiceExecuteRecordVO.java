package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 功能执行记录视图对象
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
@Data
@ExcelIgnoreUnannotated
public class ServiceExecuteRecordVO {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 功能名称
     */
    @ExcelProperty(value = "功能名称")
    private String serviceName;

    /**
     * 参数
     */
    @ExcelProperty(value = "参数")
    private String param;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;

    /**
     * 执行方式   手动触发  场景触发
     */
    @ExcelProperty(value = "执行方式   手动触发  场景触发")
    private Long executeType;

    /**
     * 执行人执行方式未手动触发时有值
     */
    @ExcelProperty(value = "执行人执行方式未手动触发时有值")
    private String executeUser;

    /**
     * 执行场景ID
     */
    @ExcelProperty(value = "执行场景ID")
    private Long executeRuleId;

    /**
     * 执行状态
     */
    private Boolean executeStatus;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "执行时间")
    public Date createTime;


}
