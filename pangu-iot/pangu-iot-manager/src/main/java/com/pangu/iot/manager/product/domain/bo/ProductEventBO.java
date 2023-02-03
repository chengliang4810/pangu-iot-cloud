package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 告警规则业务对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductEventBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 告警规则名称
     */
    @NotBlank(message = "告警规则名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 告警等级
     */
    @NotNull(message = "告警等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long level;

    /**
     * and 或者 or
     */
    @NotBlank(message = "and 或者 or不能为空", groups = { AddGroup.class, EditGroup.class })
    private String expLogic;

    /**
     * 0 否 1 是
     */
    @NotNull(message = "0 否 1 是不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long notify;

    /**
     * 0 告警 1场景联动
     */
    @NotNull(message = "0 告警 1场景联动不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long classify;

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long taskId;

    /**
     * 触发类型 0-条件触发 1-定时触发
     */
    @NotNull(message = "触发类型 0-条件触发 1-定时触发不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long triggerType;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
