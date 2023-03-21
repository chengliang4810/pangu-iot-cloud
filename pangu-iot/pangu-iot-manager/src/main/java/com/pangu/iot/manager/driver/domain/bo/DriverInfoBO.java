package com.pangu.iot.manager.driver.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 驱动属性配置信息业务对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverInfoBO extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 驱动属性ID
     */
    @NotNull(message = "驱动属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long driverAttributeId;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String description;


}
