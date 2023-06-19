package org.dromara.manager.driver.domain.bo;

import org.dromara.manager.driver.domain.DriverAttribute;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 驱动属性业务对象 iot_driver_attribute
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@AutoMapper(target = DriverAttribute.class, reverseConvertGenerate = false)
public class DriverAttributeBo {

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
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attributeName;

    /**
     * 属性类型
     */
    @NotBlank(message = "属性类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attributeType;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String displayName;

    /**
     * 默认值
     */
    @NotBlank(message = "默认值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String defaultValue;

    /**
     * 必填
     */
    @NotNull(message = "必填不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer required;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}