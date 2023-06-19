package org.dromara.manager.driver.domain.bo;

import org.dromara.manager.driver.domain.DriverAttributeValue;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 驱动属性值业务对象 iot_driver_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@AutoMapper(target = DriverAttributeValue.class, reverseConvertGenerate = false)
public class DriverAttributeValueBo extends BaseEntity {

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
     * 网关设备ID
     */
    @NotNull(message = "网关设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long gatewayDeviceId;

    /**
     * 属性类型
     */
    @NotBlank(message = "属性类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;


}
