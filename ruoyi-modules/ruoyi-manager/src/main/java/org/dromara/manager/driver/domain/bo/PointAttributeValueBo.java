package org.dromara.manager.driver.domain.bo;

import org.dromara.manager.driver.domain.PointAttributeValue;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 驱动属性值业务对象 iot_point_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = PointAttributeValue.class, reverseConvertGenerate = false)
public class PointAttributeValueBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 点位属性ID
     */
    @NotNull(message = "点位属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long pointAttributeId;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 设备属性ID
     */
    @NotNull(message = "设备属性ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceAttributeId;

    /**
     * 属性值
     */
    @NotBlank(message = "属性值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;


}