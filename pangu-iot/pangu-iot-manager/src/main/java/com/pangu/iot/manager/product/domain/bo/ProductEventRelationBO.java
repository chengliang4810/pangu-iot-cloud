package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 告警规则关系业务对象
 *
 * @author chengliang4810
 * @date 2023-02-03
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductEventRelationBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 规则ID
     */
    @NotNull(message = "规则ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long eventRuleId;

    /**
     * 关联产品或设备ID
     */
    @NotNull(message = "关联产品或设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * trigger id
     */
    @NotBlank(message = "trigger id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String zbxId;

    /**
     * 是否来自产品
     */
    @NotNull(message = "是否来自产品不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inherit;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Boolean status;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
