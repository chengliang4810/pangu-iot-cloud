package com.pangu.iot.manager.driver.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * 驱动属性配置信息业务对象
 *
 * @author chengliang4810
 * @date 2023-02-28
 */

@Data
public class PointInfoBatchBO implements Serializable {

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 设备属性id
     */
    @NotNull(message = "设备属性不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceAttributeId;

    /**
     * 属性值
     * key: 驱动属性ID driverAttributeId
     * value: 值 value
     */
    @NotEmpty(message = "属性值不能为空", groups = { AddGroup.class, EditGroup.class })
    private Map<Long, String> attributeValue;

}
