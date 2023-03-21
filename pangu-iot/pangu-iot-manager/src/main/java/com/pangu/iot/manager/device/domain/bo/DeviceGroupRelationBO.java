package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 设备与分组关系业务对象
 *
 * @author chengliang4810
 * @date 2023-01-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceGroupRelationBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 设备组ID
     */
    @NotNull(message = "设备组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceGroupId;


}
