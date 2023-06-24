package org.dromara.common.iot.entity.driver;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 驱动配置属性表
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DriverAttribute {

    /**
     * id
     */
    private Long id;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性类型标识
     */
    private String attributeTypeFlag;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 必填
     */
    private Boolean required;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 租户ID
     */
    private String tenantId;
}
