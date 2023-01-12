package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class LastDataAttributeBO {

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空")
    private Long deviceId;

}
