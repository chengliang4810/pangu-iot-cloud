package org.dromara.manager.driver.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.manager.driver.domain.PointAttributeValue;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@AutoMapper(target = PointAttributeValue.class, reverseConvertGenerate = false)
public class BatchPointAttributeValueItem implements Serializable {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 点位属性ID
     */
    @NotNull(message = "点位属性ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long pointAttributeId;

    /**
     * 属性值
     */
    @NotBlank(message = "属性值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;


}
