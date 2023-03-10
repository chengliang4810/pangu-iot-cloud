package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import com.pangu.common.core.web.domain.TreeEntity;

/**
 * 产品分组业务对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductGroupBO extends TreeEntity<ProductGroupBO> {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 所有父级ID
     */
    @NotBlank(message = "所有父级ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pids;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
