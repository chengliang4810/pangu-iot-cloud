package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 功能执行记录业务对象
 *
 * @author chengliang4810
 * @date 2023-02-14
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceExecuteRecordBO extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 功能名称
     */
    @NotBlank(message = "功能名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String serviceName;

    /**
     * 参数
     */
    @NotBlank(message = "参数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String param;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 执行方式   手动触发  场景触发
     */
    @NotNull(message = "执行方式   手动触发  场景触发不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long executeType;

    /**
     * 执行人执行方式未手动触发时有值
     */
    @NotBlank(message = "执行人执行方式未手动触发时有值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String executeUser;

    /**
     * 执行场景ID
     */
    @NotNull(message = "执行场景ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long executeRuleId;

    /**
     * 执行状态
     */
    private Boolean executeStatus;


}
