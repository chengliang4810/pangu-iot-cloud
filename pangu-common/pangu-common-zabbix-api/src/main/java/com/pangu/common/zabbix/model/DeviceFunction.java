package com.pangu.common.zabbix.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

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
     * 设备id
     */
    private String deviceId;


    /**
     * 标识符
     */
    private String identifier;


    /**
     * 参数
     */
    private Map<String, String> params;

}
