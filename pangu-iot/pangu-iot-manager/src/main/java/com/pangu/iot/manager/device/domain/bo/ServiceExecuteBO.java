package com.pangu.iot.manager.device.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 服务执行
 *
 * @author chengliang4810
 * @date 2023/02/14 11:06
 */
@Data
public class ServiceExecuteBO {

    @NotBlank
    private Long deviceId;
    @NotNull
    private Long serviceId;
    @NotBlank
    private Object value;

}
