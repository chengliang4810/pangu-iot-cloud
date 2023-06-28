package org.dromara.manager.device.domain.bo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 子设备
 *
 * @author chengliang
 * @date 2023/06/28
 */
@Data
@Accessors(chain = true)
public class ChildDeviceBo {

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空")
    private Long deviceId;

    /**
     * 子设备ID
     */
    @Size(min = 1, message = "请添加子设备")
    private List<Long> childDeviceIds;

}
