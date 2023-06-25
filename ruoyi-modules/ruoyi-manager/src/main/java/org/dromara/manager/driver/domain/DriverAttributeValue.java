package org.dromara.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 驱动属性值对象 iot_driver_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@TableName("iot_driver_attribute_value")
public class DriverAttributeValue {

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
     * 属性值
     */
    private String value;


}
