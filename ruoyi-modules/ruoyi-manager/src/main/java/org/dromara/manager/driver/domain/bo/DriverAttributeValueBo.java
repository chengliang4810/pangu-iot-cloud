package org.dromara.manager.driver.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.driver.domain.DriverAttributeValue;

/**
 * 驱动属性值业务对象 iot_driver_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@Accessors(chain = true)
@AutoMapper(target = DriverAttributeValue.class, reverseConvertGenerate = false)
public class DriverAttributeValueBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 驱动属性ID
     */
    @NotNull(message = "驱动属性ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long driverAttributeId;

    /**
     * 网关设备ID
     */
    @NotNull(message = "网关设备ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long gatewayDeviceId;

    /**
     * 属性类型
     */
    @NotBlank(message = "属性类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;


}
