package org.dromara.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 网关设备绑定子设备关系对象 iot_gateway_bind_relation
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@TableName("iot_gateway_bind_relation")
public class GatewayBindRelation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
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
