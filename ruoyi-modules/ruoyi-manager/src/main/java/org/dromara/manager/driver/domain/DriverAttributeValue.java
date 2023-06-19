package org.dromara.manager.driver.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 驱动属性值对象 iot_driver_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_driver_attribute_value")
public class DriverAttributeValue extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 驱动属性ID
     */
    private Long driverAttributeId;

    /**
     * 网关设备ID
     */
    private Long gatewayDeviceId;

    /**
     * 属性类型
     */
    private String value;


}
