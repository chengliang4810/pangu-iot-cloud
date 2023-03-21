package com.pangu.iot.manager.device.domain.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Accessors(chain = true)
public class DebugDataBO {

    /**
     * 设备id
     */
    @NotBlank(message = "设备id不能为空")
    private Long deviceId;

    /**
     * 参数
     */
    @Min(value = 1, message = "参数不能为空")
    private List<Params> params;

    @Data
    public static class Params{

        /**
         * 设备属性KEY
         */
        @NotBlank(message = "设备属性KEY不能为空")
        private String deviceAttrKey;

        /**
         * 设备属性值
         */
        @NotBlank(message = "设备属性值不能为空")
        private String deviceAttrValue;

    }


}
