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

package org.dromara.common.sdk;

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.entity.driver.AttributeInfo;
import io.github.pnoker.common.entity.driver.DriverMetadata;
import io.github.pnoker.common.enums.DriverStatusEnum;
import io.github.pnoker.common.exception.NotFoundException;
import io.github.pnoker.common.model.Device;
import io.github.pnoker.common.model.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
public class DriverContext {

    /**
     * 驱动 状态
     */
    private DriverStatusEnum driverStatus = DriverStatusEnum.OFFLINE;

    /**
     * 驱动 元数据，当且仅当驱动注册成功之后由 Manager 返回
     */
    private DriverMetadata driverMetadata = new DriverMetadata();

    /**
     * 根据 设备Id 获取连接设备的驱动配置信息
     *
     * @param deviceId 设备ID
     * @return Map String:AttributeInfo
     */
    public Map<String, AttributeInfo> getDriverInfoByDeviceId(String deviceId) {
        return this.driverMetadata.getDriverInfoMap().get(deviceId);
    }

    /**
     * 根据 设备Id 获取连接设备的全部位号配置信息
     *
     * @param deviceId 设备ID
     * @return Map String:(Map String:AttributeInfo)
     */
    public Map<String, Map<String, AttributeInfo>> getPointInfoByDeviceId(String deviceId) {
        Map<String, Map<String, AttributeInfo>> tmpMap = this.driverMetadata.getPointInfoMap().get(deviceId);
        if (ObjectUtil.isNull(tmpMap) || tmpMap.size() < 1) {
            throw new NotFoundException("Device({}) does not exist", deviceId);
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 和 位号Id 获取连接设备的位号配置信息
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return Map String:AttributeInfo
     */
    public Map<String, AttributeInfo> getPointInfoByDeviceIdAndPointId(String deviceId, String pointId) {
        Map<String, AttributeInfo> tmpMap = getPointInfoByDeviceId(deviceId).get(pointId);
        if (ObjectUtil.isNull(tmpMap) || tmpMap.size() < 1) {
            throw new NotFoundException("Point({}) info does not exist", pointId);
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 获取设备
     *
     * @param deviceId 设备ID
     * @return Device
     */
    public Device getDeviceByDeviceId(String deviceId) {
        Device device = this.driverMetadata.getDeviceMap().get(deviceId);
        if (ObjectUtil.isNull(device)) {
            throw new NotFoundException("Device({}) does not exist", deviceId);
        }
        return device;
    }

    /**
     * 根据 设备Id 获取位号
     *
     * @param deviceId 设备ID
     * @return Point Array
     */
    public List<Point> getPointByDeviceId(String deviceId) {
        Device device = getDeviceByDeviceId(deviceId);
        return this.driverMetadata.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(entry -> new ArrayList<>(entry.getValue().values()))
                .reduce(new ArrayList<>(), (total, temp) -> {
                    total.addAll(temp);
                    return total;
                });
    }

    /**
     * 根据 设备Id和位号Id 获取位号
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return Point
     */
    public Point getPointByDeviceIdAndPointId(String deviceId, String pointId) {
        // TODO 当设备量很大的时候，建议用本地数据库来存储设备数据，弄个热数据缓存，仅仅把经常使用的数据放到内存中来
        Device device = getDeviceByDeviceId(deviceId);
        Optional<Map<String, Point>> optional = this.driverMetadata.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(entry -> entry.containsKey(pointId))
                .findFirst();

        if (optional.isPresent()) {
            return optional.get().get(pointId);
        }

        throw new NotFoundException("Point({}) info does not exist", pointId);
    }

}
