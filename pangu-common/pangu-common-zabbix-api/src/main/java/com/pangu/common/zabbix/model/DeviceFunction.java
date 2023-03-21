package com.pangu.common.zabbix.model;

import com.pangu.common.core.domain.dto.AttributeInfo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设备功能
 *
 * @author chengliang
 * @date 2023/02/17
 */
@Data
@Accessors(chain = true)
public class DeviceFunction implements Serializable {

    /**
     * 流水编号
     */
    private Long uuid;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 功能id
     */
    private Long serviceId;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 参数
     */
    private AttributeInfo value;

}
