package com.pangu.iot.manager.device.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DeviceGatewayBindBo {

    @NotNull(message = "网关设备ID不能为空")
    private Long gatewayDeviceId;

    @NotNull(message = "设备ID不能为空")
    private List<Long> deviceIds;

}
