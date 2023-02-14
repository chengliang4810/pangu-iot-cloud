package com.pangu.iot.manager.device.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 服务执行博
 *
 * @author chengliang4810
 * @date 2023/02/14 11:06
 */
@Data
public class ServiceExecuteBO {

    @NotBlank
    private Long deviceId;
    @NotNull
    private Long   serviceId;

    private List<ServiceParam> serviceParams;

    @Data
    public static class ServiceParam {
        @NotBlank
        private String key;
        @NotBlank
        private String value;
    }


}
