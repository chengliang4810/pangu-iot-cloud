package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备与分组关系对象 iot_device_group_relation
 *
 * @author chengliang4810
 * @date 2023-01-07
 */
@Data
@Accessors(chain = true)
@TableName("iot_device_group_relation")
public class DeviceGroupRelation {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备组ID
     */
    private Long deviceGroupId;

    /** the constant of field {@link DeviceGroupRelation#deviceGroupId} */
    public static final String CONST_DEVICE_GROUP_ID = "device_group.device_group_id";
}
