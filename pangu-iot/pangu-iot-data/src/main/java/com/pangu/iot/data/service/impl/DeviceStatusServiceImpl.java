package com.pangu.iot.data.service.impl;

import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.iot.data.service.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.pangu.common.core.constant.IotConstants.DEVICE_STATUS_CACHE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceStatusServiceImpl implements DeviceStatusService  {

    /**
     * 设备状态变更
     *
     * @param deviceId 设备ID
     * @param status   状态
     * @param productId  产品Id
     */
    @Override
    public void changeStatus(String productId, String deviceId, Integer status) {
        if (1 == status){
            RedisUtils.setCacheObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId, status);
        }else {
            RedisUtils.deleteObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId);
        }
    }


    /**
     * 获得设备状态
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return int
     */
    @Override
    public int getStatus(String productId, String deviceId) {
        Integer status = RedisUtils.getCacheObject(DEVICE_STATUS_CACHE_PREFIX + productId + "_" + deviceId);
        return status == null ? 0 : status;
    }
}
