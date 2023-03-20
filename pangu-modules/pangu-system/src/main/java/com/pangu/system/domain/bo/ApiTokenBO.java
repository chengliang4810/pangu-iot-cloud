package com.pangu.system.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 三方授权业务对象
 *
 * @author chengliang4810
 * @date 2023-03-14
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiTokenBO extends BaseEntity {

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 状态（0正常 1停用）
     */
    @NotNull(message = "状态（0正常 1停用）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Boolean status;

    /**
     * 备注
     */
    private String remark;


}
