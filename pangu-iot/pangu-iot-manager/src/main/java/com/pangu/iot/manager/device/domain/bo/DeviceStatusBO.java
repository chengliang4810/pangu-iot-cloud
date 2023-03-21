package com.pangu.iot.manager.device.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 设备状态
 *
 * @author chengliang
 * @date 2023/01/08
 */
@Data
@Accessors(chain = true)
public class DeviceStatusBO {

    /**
     * 设备id
     */
    @NotNull(message = "设备主键不能为空")
    private Long deviceId;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Boolean status;

}
