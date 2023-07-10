package org.dromara.common.sdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.enums.DriverStatusEnum;
import org.dromara.common.iot.model.Point;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author pnoker,chengliang4810
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
    public Map<String, AttributeInfo> getDriverInfoByDeviceId(Long deviceId) {
        return this.driverMetadata.getDriverInfoMap().get(deviceId);
    }

    /**
     * 根据 设备Id 获取连接设备的全部位号配置信息
     *
     * @param deviceId 设备ID
     * @return Map String:(Map String:AttributeInfo)
     */
    public Map<Long, Map<String, AttributeInfo>> getPointInfoByDeviceId(Long deviceId) {
        Map<Long, Map<String, AttributeInfo>> tmpMap = this.driverMetadata.getPointInfoMap().get(deviceId);
        if (MapUtils.isEmpty(tmpMap)) {
            throw new ServiceException("Device({" + deviceId + "}) does not exist");
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
    public Map<String, AttributeInfo> getPointInfoByDeviceIdAndPointId(Long deviceId, Long pointId) {
        Map<String, AttributeInfo> tmpMap = getPointInfoByDeviceId(deviceId).get(pointId);
        if (MapUtils.isEmpty(tmpMap)) {
            throw new ServiceException("Point({"+pointId+"}) info does not exist");
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 获取设备
     *
     * @param deviceId 设备ID
     * @return Device
     */
    public Device getDeviceByDeviceId(Long deviceId) {
        Device device = this.driverMetadata.getDeviceMap().get(deviceId);
        if (ObjectUtils.anyNull(device)) {
            throw new ServiceException("Device({"+deviceId+"}) does not exist");
        }
        return device;
    }

    /**
     * 根据 设备Id 获取位号
     *
     * @param deviceId 设备ID
     * @return Point Array
     */
    public List<Point> getPointByDeviceId(Long deviceId) {
        Device device = getDeviceByDeviceId(deviceId);
        return null;
//        return this.driverMetadata.getProfilePointMap().entrySet().stream()
//                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
//                .map(entry -> new ArrayList<>(entry.getValue().values()))
//                .reduce(new ArrayList<>(), (total, temp) -> {
//                    total.addAll(temp);
//                    return total;
//                });
    }

    /**
     * 根据 设备Id和位号Id 获取位号
     *
     * @param deviceId 设备ID
     * @param pointId  位号ID
     * @return Point
     */
    public Point getPointByDeviceIdAndPointId(Long deviceId, Long pointId) {
        // TODO 当设备量很大的时候，建议用本地数据库来存储设备数据，弄个热数据缓存，仅仅把经常使用的数据放到内存中来
//        Device device = getDeviceByDeviceId(deviceId);
//        Optional<Map<String, Point>> optional = this.driverMetadata.getProfilePointMap().entrySet().stream()
//                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
//                .map(Map.Entry::getValue)
//                .filter(entry -> entry.containsKey(pointId))
//                .findFirst();
//
//        if (optional.isPresent()) {
//            return optional.get().get(pointId);
//        }
        throw new ServiceException("Point(" + pointId + ") info does not exist");
    }

}
