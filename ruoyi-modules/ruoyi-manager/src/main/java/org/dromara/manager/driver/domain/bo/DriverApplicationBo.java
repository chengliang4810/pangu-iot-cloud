package org.dromara.manager.driver.domain.bo;

import org.dromara.manager.driver.domain.DriverApplication;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 驱动应用业务对象 iot_driver_application
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DriverApplication.class, reverseConvertGenerate = false)
public class DriverApplicationBo extends BaseEntity {

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
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applicationName;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String host;

    /**
     * 端口号
     */
    @NotNull(message = "端口号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long port;


}
