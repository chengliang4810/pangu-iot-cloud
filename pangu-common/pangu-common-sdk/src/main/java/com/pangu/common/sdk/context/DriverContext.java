package com.pangu.common.sdk.context;

import cn.hutool.core.map.MapUtil;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.manager.api.domain.DriverMetadata;
import com.pangu.manager.api.enums.OnlineStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Slf4j
@Component
public class DriverContext {

    private DriverMetadata driverMetadata = new DriverMetadata();

    /**
     * 驱动 状态，默认为 未注册 状态
     */
    private OnlineStatus driverStatus = OnlineStatus.OFFLINE;

    public synchronized void setDriverStatus(OnlineStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public void setDriverMetadata(DriverMetadata driverMetadata) {
        this.driverMetadata = driverMetadata;
    }


    /**
     * 根据 设备Id 获取连接设备的驱动配置信息
     *
     * @param deviceId Device Id
     * @return Map<String, AttributeInfo>
     */
    public Map<String, AttributeInfo> getDriverInfoByDeviceId(Long deviceId) {
        return this.driverMetadata.getDriverInfoMap().get(deviceId);
    }

    /**
     * 根据 设备Id 获取连接设备的全部位号配置信息
     *
     * @param deviceId Device Id
     * @return Map<String, Map < String, AttributeInfo>>
     */
    public Map<Long, Map<String, AttributeInfo>> getPointInfoByDeviceId(Long deviceId) {
        Map<Long, Map<String, AttributeInfo>> tmpMap = this.driverMetadata.getPointInfoMap().get(deviceId);
        if (MapUtil.isEmpty(tmpMap)) {
            log.warn("Device({}) does not exist", deviceId);
            return Collections.emptyMap();
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 和 位号Id 获取连接设备的位号配置信息
     *
     * @param deviceId Device Id
     * @param pointId  Point Id
     * @return Map<String, AttributeInfo>
     */
    public Map<String, AttributeInfo> getPointInfoByDeviceIdAndPointId(Long deviceId, Long pointId) {
        Map<String, AttributeInfo> tmpMap = getPointInfoByDeviceId(deviceId).get(pointId);
        if (null == tmpMap || tmpMap.size() < 1) {
            log.warn("Point( {} ) info does not exist", pointId);
            return Collections.emptyMap();
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 获取设备
     *
     * @param deviceId Device Id
     * @return Device
     */
    public Device getDeviceByDeviceId(String deviceId) {
        Device device = this.driverMetadata.getDeviceMap().get(deviceId);
        if (null == device) {
            log.warn("Device({}) does not exist", deviceId);
            throw new ServiceException("Device does not exist");
        }
        return device;
    }

    /**
     * 根据 设备Id 获取位号
     *
     * @param deviceId Device Id
     * @return Point Array
     */
    public List<DeviceAttribute> getPointByDeviceId(String deviceId) {
        Device device = getDeviceByDeviceId(deviceId);
        return this.driverMetadata.getProfileAttributeMap().values().stream()
            .flatMap(map -> map.values().stream())
            .filter(deviceAttribute -> deviceAttribute.getDeviceId().equals(device.getId()))
            .collect(Collectors.toList());
    }

    /**
     * 根据 设备Id和位号Id 获取位号
     *
     * @param deviceId Device Id
     * @param attributeId  DeviceAttribute Id
     * @return DeviceAttribute
     */
    public DeviceAttribute getPointByDeviceIdAndAttributeId(String deviceId, String attributeId) {
        Device device = getDeviceByDeviceId(deviceId);
        Optional<Map<Long, DeviceAttribute>> optional = this.driverMetadata.getProfileAttributeMap().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(entry -> entry.containsKey(attributeId))
            .findFirst();

        if (optional.isPresent()) {
            return optional.get().get(attributeId);
        }

        throw new ServiceException("Point(" + attributeId + ") point does not exist");
    }

}
