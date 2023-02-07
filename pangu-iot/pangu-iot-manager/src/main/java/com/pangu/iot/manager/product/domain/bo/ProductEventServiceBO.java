package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 告警规则与功能关系业务对象
 *
 * @author chengliang4810
 * @date 2023-02-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductEventServiceBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 告警规则ID
     */
    @NotNull(message = "告警规则ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long eventRuleId;

    /**
     * 功能ID
     */
    @NotNull(message = "功能ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long serviceId;

    /**
     * 产品、设备ID
     */
    @NotNull(message = "产品、设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * 执行目标设备ID
     */
    @NotNull(message = "执行目标设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long executeDeviceId;


}
