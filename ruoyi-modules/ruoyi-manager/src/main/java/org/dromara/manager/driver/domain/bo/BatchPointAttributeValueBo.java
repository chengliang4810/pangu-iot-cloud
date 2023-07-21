package org.dromara.manager.driver.domain.bo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class BatchPointAttributeValueBo implements Serializable {


    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deviceId;

    /**
     * 物模型属性ID
     */
    @NotNull(message = "物模型属性ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deviceAttributeId;

    /**
     * 点位属性配置
     */
    @Size(min = 1, message = "点位属性配置不能为空", groups = {AddGroup.class, EditGroup.class})
    private List<BatchPointAttributeValueItem> pointAttributeConfig;

}
