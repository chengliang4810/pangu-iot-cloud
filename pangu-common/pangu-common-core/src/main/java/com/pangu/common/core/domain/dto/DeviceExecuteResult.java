package com.pangu.common.core.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class DeviceExecuteResult implements Serializable {

    /**
     * 流水号
     */
    private Long uuid;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 服务id
     */
    private Long serviceId;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 成功
     */
    private Boolean success;


}
