package org.dromara.common.iot.entity.device;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 位号数据
 *
 * @author pnoker,chengliang4810
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceValue implements Serializable {

    /**
     * 设备 CODE
     */
    private String deviceCode;

    /**
     * 属性 key : value
     */
    private Map<String, String> attributes;

    /**
     * 采集时间
     */
    private Date originTime;

    public DeviceValue(String deviceCode, String key, String value) {
        this.deviceCode = deviceCode;
        if (CollUtil.isEmpty(attributes)) {
            attributes = new HashMap<>(5);
        }
        attributes.put(key, value);
        this.originTime = new Date();
    }

    public DeviceValue(String deviceCode, Map<String, String> attributes) {
        this.deviceCode = deviceCode;
        this.attributes = attributes;
        this.originTime = new Date();
    }

}
