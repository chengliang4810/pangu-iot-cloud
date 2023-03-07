package com.pangu.common.zabbix.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * 设备数据
 *
 * @author chengliang4810
 * @date 2023/01/09 11:24
 */

@Getter
@Setter
@NoArgsConstructor
public class DeviceValue {

    /**
     * 设备唯一ID 对应数据库device_code字段，非主键
     */
    private String deviceId;

    /**
     * 属性 key : value
     */
    private Map<String, String> attributes;

    /**
     *  毫秒，70年到现在时间戳
     */
    private Long clock;


    public DeviceValue(String deviceId, Map<String, String> attributes) {
        this.deviceId = deviceId;
        this.attributes = attributes;
        this.clock = System.currentTimeMillis() / 1000;
    }
}
