package org.dromara.common.iot.entity.driver;

import lombok.Data;
import org.dromara.common.iot.entity.point.PointAttribute;

import java.awt.*;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Driver Metadata
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
public class DriverMetadata implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long driverId;
    private String tenantId;
    private Map<Long, DriverAttribute> driverAttributeMap;
    private Map<Long, PointAttribute> pointAttributeMap;

    /**
     * 网关设备
     * deviceId,device
     */
    private Map<Long, Device> gatewayDeviceMap;

    /**
     * deviceId,device
     */
    private Map<Long, Device> deviceMap;

    /**
     * deviceId(driverAttribute.name,(driverInfo.value,driverAttribute.type))
     */
    private Map<Long, Map<String, AttributeInfo>> driverInfoMap;

    /**
     * deviceId(pointId(pointAttribute.name,(pointInfo.value,pointAttribute.type)))
     */
    private Map<Long, Map<Long, Map<String, AttributeInfo>>> pointInfoMap;

    /**
     * profileId(pointId,point)
     */
    private Map<Long, Map<Long, Point>> profilePointMap;

    public DriverMetadata() {
        this.driverAttributeMap = new ConcurrentHashMap<>(16);
        this.pointAttributeMap = new ConcurrentHashMap<>(16);
        this.deviceMap = new ConcurrentHashMap<>(16);
        this.driverInfoMap = new ConcurrentHashMap<>(16);
        this.pointInfoMap = new ConcurrentHashMap<>(16);
        this.profilePointMap = new ConcurrentHashMap<>(16);
    }
}
