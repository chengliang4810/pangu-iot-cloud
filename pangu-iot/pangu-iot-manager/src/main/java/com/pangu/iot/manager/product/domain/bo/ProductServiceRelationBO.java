package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 产品功能关联关系业务对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductServiceRelationBO extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 服务ID
     */
    @NotNull(message = "服务ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long serviceId;

    /**
     * 关联ID
     */
    @NotNull(message = "关联ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long relationId;

    /**
     * 是否继承
     */
    @NotNull(message = "是否继承不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long inherit;


}
