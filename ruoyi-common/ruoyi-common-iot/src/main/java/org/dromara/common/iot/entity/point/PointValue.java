package org.dromara.common.iot.entity.point;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * MongoDB 位号数据
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 设备ID，同MySQl中等 设备ID 一致
     */
    private String deviceId;

    /**
     * 位号ID，同MySQl中等 位号ID 一致
     */
    private String pointId;

    /**
     * 处理值，进行过缩放、格式化等操作
     */
    private String value;

    /**
     * 原始值
     */
    private String rawValue;

    private List<String> children;

    private Date originTime;

    private Date createTime;

    public PointValue(String deviceId, String pointId, String rawValue, String value) {
        this.deviceId = deviceId;
        this.pointId = pointId;
        this.rawValue = rawValue;
        this.value = value;
        this.originTime = new Date();
    }
}
