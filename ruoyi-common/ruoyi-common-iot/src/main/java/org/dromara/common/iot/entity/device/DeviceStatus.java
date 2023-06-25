package org.dromara.common.iot.entity.device;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.common.iot.enums.DeviceStatusEnum;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 设备状态模型
 *
 * @author chengliang
 * @date 2023/06/25
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class DeviceStatus implements Serializable {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备状态
     */
    private DeviceStatusEnum status;

    /**
     * 时间
     */
    private Long time;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 默认时间
     */
    public static final Long DEFAULT_TIME = 30L;

    /**
     * 默认单位
     */
    public static final TimeUnit DEFAULT_UNIT = TimeUnit.SECONDS;

    /**
     * 时间
     * 如果为空则返回默认时间
     * @return {@link Long}
     */
    public Long getTime(){
        return ObjectUtil.isNull(time) ? DEFAULT_TIME : time;
    }

    /**
     * 时间单位
     * 如果为空则返回默认单位
     * @return {@link TimeUnit}
     */
    public TimeUnit getTimeUnit(){
        return ObjectUtil.isNull(timeUnit) ? DEFAULT_UNIT : timeUnit;
    }

    /**
     * 获得状态
     * 如果为空则返回离线状态
     * @return {@link DeviceStatusEnum}
     */
    public DeviceStatusEnum getStatus(){
        return ObjectUtil.isNull(status) ? DeviceStatusEnum.OFFLINE : status;
    }

    /**
     * @param deviceId 设备id
     * @param status   状态
     *                 默认时间30秒
     *                 默认单位秒
     * @return {@link DeviceStatus}
     */
    public static DeviceStatus of(Long deviceId, DeviceStatusEnum status) {
        return  DeviceStatus.of(deviceId, status, DEFAULT_TIME, DEFAULT_UNIT);
    }

    /**
     * @param deviceId 设备id
     * @param status   状态
     * @param time     时间 单位秒
     * @return {@link DeviceStatus}
     */
    public static DeviceStatus of(Long deviceId, DeviceStatusEnum status, long time) {
        return DeviceStatus.of(deviceId, status, time, DEFAULT_UNIT);
    }

    /**
     * @param deviceId 设备id
     * @param status   状态
     * @param time     时间
     * @param timeUnit     时间单位
     * @return {@link DeviceStatus}
     */
    public static DeviceStatus of(Long deviceId, DeviceStatusEnum status, long time, TimeUnit timeUnit) {
        return new DeviceStatus(deviceId, status, time, timeUnit);
    }


    /**
     * 在线
     *
     * @param deviceId 设备id
     * @return {@link DeviceStatus}
     */
    public static DeviceStatus online(Long deviceId) {
        return new DeviceStatus(deviceId, DeviceStatusEnum.ONLINE, DEFAULT_TIME, DEFAULT_UNIT);
    }

    /**
     * 在线
     *
     * @param deviceId 设备id
     * @return {@link DeviceStatus}
     */
    public static DeviceStatus online(Long deviceId, long time, TimeUnit timeUnit) {
        return new DeviceStatus(deviceId, DeviceStatusEnum.ONLINE, time, timeUnit);
    }

}
