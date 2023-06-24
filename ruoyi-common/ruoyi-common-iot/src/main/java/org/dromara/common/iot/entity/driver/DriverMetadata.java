/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    private String driverId;
    private String tenantId;
    private Map<String, DriverAttribute> driverAttributeMap;
    private Map<String, PointAttribute> pointAttributeMap;


    /**
     * 网关设备
     * deviceId,device
     */
    private Map<String, Device> gatewayDeviceMap;

    /**
     * deviceId(driverAttribute.name,(driverInfo.value,driverAttribute.type))
     */
    private Map<String, Map<String, AttributeInfo>> driverInfoMap;

    /**
     * deviceId(pointId(pointAttribute.name,(pointInfo.value,pointAttribute.type)))
     */
    private Map<String, Map<String, Map<String, AttributeInfo>>> pointInfoMap;

    /**
     * deviceId,device
     */
    private Map<String, Device> deviceMap;

    /**
     * profileId(pointId,point)
     */
    private Map<String, Map<String, Point>> profilePointMap;

    public DriverMetadata() {
        this.driverAttributeMap = new ConcurrentHashMap<>(16);
        this.pointAttributeMap = new ConcurrentHashMap<>(16);
        this.deviceMap = new ConcurrentHashMap<>(16);
        this.driverInfoMap = new ConcurrentHashMap<>(16);
        this.pointInfoMap = new ConcurrentHashMap<>(16);
        this.profilePointMap = new ConcurrentHashMap<>(16);
    }
}
