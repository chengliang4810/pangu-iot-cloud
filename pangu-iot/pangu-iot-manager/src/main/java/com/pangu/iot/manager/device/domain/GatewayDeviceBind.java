package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备分组对象 iot_device_group
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Data
@TableName("iot_gateway_device_bind")
public class GatewayDeviceBind implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 网关设备ID
     */
    private Long gatewayDeviceId;

    /**
     * 设备ID
     */
    private Long deviceId;

}
