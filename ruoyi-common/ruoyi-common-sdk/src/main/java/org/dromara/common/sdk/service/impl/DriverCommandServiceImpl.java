package org.dromara.common.sdk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.iot.entity.device.DeviceValue;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.model.Point;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.service.DriverCommandService;
import org.dromara.common.sdk.service.DriverCustomService;
import org.dromara.common.sdk.service.DriverSenderService;
import org.dromara.common.sdk.utils.ConvertUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverCommandServiceImpl implements DriverCommandService {

    private final DriverContext driverContext;
    private final DriverSenderService driverSenderService;
    private final DriverCustomService driverCustomService;


    @Override
    public DeviceValue read(Long deviceId) {
        List<Point> pointList = driverContext.getPointByDeviceId(deviceId);
        if (CollUtil.isEmpty(pointList)) {
            throw new ServiceException("The device has no point");
        }

        // 循环设备所有属性点，读取属性点值，发送至消息队列
        Map<String, String> attributeValueMap = new HashMap<>(pointList.size());
        for (Point point : pointList) {
            try {
                DeviceValue deviceValue = read(deviceId, point.getId(), false);
                if (CollUtil.isNotEmpty(deviceValue.getAttributes())) {
                    attributeValueMap.putAll(deviceValue.getAttributes());
                }
            } catch (Exception ignored) {}
        }

        if (CollUtil.isEmpty(attributeValueMap)) {
            return null;
        }

        // 发送并返回
        Device device = driverContext.getDeviceByDeviceId(deviceId);
        DeviceValue deviceValue = new DeviceValue(device.getDeviceCode(), attributeValueMap);
        driverSenderService.pointValueSender(deviceValue);
        return deviceValue;
    }

    @Override
    public DeviceValue read(Long deviceId, Long pointId) {
        return read(deviceId, pointId, true);
    }

    private DeviceValue read(Long deviceId, Long pointId, Boolean senderValue) {
        if (null == senderValue){
            senderValue = true;
        }

        Device device = driverContext.getDeviceByDeviceId(deviceId);
        Point point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);

        try {
            String rawValue = driverCustomService.read(
                    driverContext.getDriverInfoByDeviceId(deviceId),
                    driverContext.getPointInfoByDeviceIdAndPointId(deviceId, pointId),
                    device,
                    driverContext.getPointByDeviceIdAndPointId(deviceId, pointId)
            );

            if (CharSequenceUtil.isEmpty(rawValue)) {
                throw new ServiceException("The read point value is null");
            }

            DeviceValue deviceValue = new DeviceValue(device.getDeviceCode(), point.getIdentifier(), ConvertUtil.convertValue(point, rawValue));
            if (senderValue){
                driverSenderService.pointValueSender(deviceValue);
            }
            return deviceValue;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

//    @Override
//    public void read(DeviceCommandDTO commandDTO) {
//        DeviceCommandDTO.DeviceRead deviceRead = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceRead.class);
//        if (ObjectUtil.isNull(deviceRead)) {
//            return;
//        }
//
//        log.info("Start command of read: {}", JsonUtil.toPrettyJsonString(commandDTO));
//        PointValue read = read(deviceRead.getDeviceId(), deviceRead.getPointId());
//        log.info("End command of read: {}", JsonUtil.toPrettyJsonString(read));
//    }

    @Override
    public Boolean write(Long deviceId, Long pointId, String value) {
        return false;
//        Device device = driverContext.getDeviceByDeviceId(deviceId);
//        try {
//            Point point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);
//            AttributeTypeFlagEnum typeEnum = AttributeTypeFlagEnum.ofCode(point.getPointTypeFlag().getCode());
//            return driverCustomService.write(
//                    driverContext.getDriverInfoByDeviceId(deviceId),
//                    driverContext.getPointInfoByDeviceIdAndPointId(deviceId, pointId),
//                    device,
//                    new AttributeInfo(value, typeEnum)
//            );
//        } catch (Exception e) {
//            throw new ServiceException(e.getMessage());
//        }
    }

//    @Override
//    public void write(DeviceCommandDTO commandDTO) {
//        DeviceCommandDTO.DeviceWrite deviceWrite = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceWrite.class);
//        if (ObjectUtil.isNull(deviceWrite)) {
//            return;
//        }
//
//        log.info("Start command of write: {}", JsonUtil.toPrettyJsonString(commandDTO));
//        Boolean write = write(deviceWrite.getDeviceId(), deviceWrite.getPointId(), deviceWrite.getValue());
//        log.info("End command of write: write {}", write);
//    }

}
