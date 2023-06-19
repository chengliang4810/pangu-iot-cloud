package org.dromara.manager.driver.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 驱动应用对象 iot_driver_application
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_driver_application")
public class DriverApplication extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 显示名称
     */
    private String host;

    /**
     * 端口号
     */
    private Long port;


}
