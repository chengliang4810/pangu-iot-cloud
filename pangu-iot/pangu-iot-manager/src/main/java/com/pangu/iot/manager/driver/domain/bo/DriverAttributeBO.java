package com.pangu.iot.manager.driver.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 驱动属性业务对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverAttributeBO extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 驱动ID
     */
    @NotNull(message = "驱动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long driverId;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String displayName;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 类型
     */
    @NotBlank(message = "类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 默认值
     */
    @NotBlank(message = "默认值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;


}
