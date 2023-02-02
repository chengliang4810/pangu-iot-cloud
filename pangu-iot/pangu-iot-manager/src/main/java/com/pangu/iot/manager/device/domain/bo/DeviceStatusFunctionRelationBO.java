package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 设备上下线规则与设备关系业务对象
 *
 * @author chengliang4810
 * @date 2023-02-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceStatusFunctionRelationBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规则ID
     */
    @NotNull(message = "规则ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long ruleId;

    /**
     * 产品/设备ID
     */
    @NotNull(message = "产品/设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * zbx下线触发器主键
     */
    @NotNull(message = "zbx下线触发器主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long zbxId;

    /**
     * zbx上线触发器主键
     */
    @NotNull(message = "zbx上线触发器主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long zbxIdRecovery;


}
