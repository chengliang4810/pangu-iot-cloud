package com.pangu.iot.manager.driver.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 协议驱动业务对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DriverBO extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 协议名称
     */
    @NotBlank(message = "协议名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String displayName;

    /**
     * 协议服务名称
     */
    @NotBlank(message = "协议服务名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String serviceName;

    /**
     * 主机IP
     */
    @NotBlank(message = "主机IP不能为空", groups = { AddGroup.class, EditGroup.class })
    private String host;

    /**
     * 端口
     */
    @NotNull(message = "端口不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long port;

    /**
     * 启用|禁用
     */
    @NotNull(message = "启用|禁用不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long enable;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;


}
