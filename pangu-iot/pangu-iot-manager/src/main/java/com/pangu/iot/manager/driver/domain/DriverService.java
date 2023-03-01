package com.pangu.iot.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 驱动服务对象 iot_driver_service
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
@Data
@Accessors(chain = true)
@TableName("iot_driver_service")
public class DriverService implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private String id;
    /**
     * 驱动ID
     */
    private Long driverId;
    /**
     * 协议服务名称
     */
    private String serviceName;
    /**
     * 主机IP
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

}
