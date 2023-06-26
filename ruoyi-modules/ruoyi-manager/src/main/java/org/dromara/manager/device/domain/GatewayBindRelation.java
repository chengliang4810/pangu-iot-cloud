package org.dromara.manager.device.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 网关设备绑定子设备关系对象 iot_gateway_bind_relation
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_gateway_bind_relation")
public class GatewayBindRelation extends BaseEntity {

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
