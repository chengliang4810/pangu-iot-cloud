package org.dromara.common.iot.entity.driver;


import lombok.*;


/**
 * 设备表
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Device {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 产品id
     */
    private Long productId;
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 驱动ID
     */
    private String driverId;

    /**
     * 分组ID
     */
    private String groupId;

    /**
     * 租户ID
     */
    private String tenantId;

}
