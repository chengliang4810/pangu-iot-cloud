package org.dromara.common.sdk;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.enums.DriverStatusEnum;
import org.dromara.common.iot.model.Point;
import org.springframework.stereotype.Component;

import java.util.*;

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
        if (CollUtil.isEmpty(tmpMap)) {
            return Collections.emptyMap();
            //throw new ServiceException("Device({" + deviceId + "}) does not exist");
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
        if (CollUtil.isEmpty(tmpMap)) {
            return Collections.emptyMap();
            // throw new ServiceException("Point({"+pointId+"}) info does not exist");
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
        if (null == device) {
            device = this.getDriverMetadata().getGatewayDeviceMap().get(deviceId);
        }
        if (ObjUtil.isNull(device)) {
            throw new ServiceException("Device({" + deviceId + "}) does not exist");
        }
        return device;
    }

    /**
     * 获取位号列表
     *
     * @return Point Array
     */
    public List<Point> getPointList() {
        return new ArrayList<>(this.driverMetadata.getPointMap().values());
    }

    /**
     * 根据 位号Id 获取位号
     *
     * @param pointId  位号ID
     * @return Point
     */
    public Point getPointByPointId(Long pointId) {
        // TODO 当设备量很大的时候，建议用本地数据库来存储设备数据，弄个热数据缓存，仅仅把经常使用的数据放到内存中来
        Optional<Point> optional = this.driverMetadata.getPointMap().values().stream().filter(point -> point.getId().equals(pointId)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ServiceException("Point(" + pointId + ") info does not exist");
    }

}
