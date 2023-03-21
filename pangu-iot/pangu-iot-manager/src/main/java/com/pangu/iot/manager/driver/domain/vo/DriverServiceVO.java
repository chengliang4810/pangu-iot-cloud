package com.pangu.iot.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 驱动服务视图对象
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
@Data
@ExcelIgnoreUnannotated
public class DriverServiceVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private String id;

    /**
     * 驱动ID
     */
    @ExcelProperty(value = "驱动ID")
    private Long driverId;

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
    private Integer port;

    /**
     * 在线状态
     */
    @ExcelProperty(value = "状态")
    private Boolean onlineStatus = false;

}
