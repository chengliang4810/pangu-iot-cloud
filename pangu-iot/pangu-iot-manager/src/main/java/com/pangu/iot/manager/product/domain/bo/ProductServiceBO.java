package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 产品功能业务对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductServiceBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 功能名称
     */
    @NotBlank(message = "功能名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 功能标识
     */
    private String mark;

    /**
     * 备注
     */
    private String remark;

    /**
     * 执行方式 0-同步 1-异步
     */
    @NotNull(message = "执行方式 0-同步 1-异步不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long async;

    /**
     * 数据类型
     */
    @NotBlank(message = "数据类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dataType;

    /**
     * 规格
     */
    @NotBlank(message = "规格不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specs;

    /**
     * 产品id
     */
    @NotNull(groups = AddGroup.class)
    private Long prodId;

    /**
     * 关系id
     */
    private Long relationId;

}
