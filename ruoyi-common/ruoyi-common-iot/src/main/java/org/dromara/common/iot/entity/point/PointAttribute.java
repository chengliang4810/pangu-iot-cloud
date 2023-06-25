package org.dromara.common.iot.entity.point;


import lombok.*;


/**
 * 模板配置信息表
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class PointAttribute {

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
     * 必填
     */
    private Boolean required = true;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 描述
     */
    private String remark;
}
