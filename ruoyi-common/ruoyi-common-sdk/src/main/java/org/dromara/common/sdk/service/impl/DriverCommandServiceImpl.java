package org.dromara.common.sdk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.sdk.service.DriverCommandService;
import org.springframework.stereotype.Service;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverCommandServiceImpl implements DriverCommandService {

//    @Resource
//    private DriverContext driverContext;
//    @Resource
//    private DriverSenderService driverSenderService;
//    @Resource
//    private DriverCustomService driverCustomService;
//
//    @Override
//    public PointValue read(String deviceId, String pointId) {
//        Device device = driverContext.getDeviceByDeviceId(deviceId);
//        Point point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);
//
//        try {
//            String rawValue = driverCustomService.read(
//                    driverContext.getDriverInfoByDeviceId(deviceId),
//                    driverContext.getPointInfoByDeviceIdAndPointId(deviceId, pointId),
//                    device,
//                    driverContext.getPointByDeviceIdAndPointId(deviceId, pointId)
//            );
//
//            if (CharSequenceUtil.isEmpty(rawValue)) {
//                throw new ReadPointException("The read point value is null");
//            }
//            if (DefaultConstant.DEFAULT_VALUE.equals(rawValue)) {
//                throw new ReadPointException(CharSequenceUtil.format("The read point value is invalid: {}", rawValue));
//            }
//
//            PointValue pointValue = new PointValue(deviceId, pointId, rawValue, ConvertUtil.convertValue(point, rawValue));
//            driverSenderService.pointValueSender(pointValue);
//            return pointValue;
//        } catch (Exception e) {
//            throw new ServiceException(e.getMessage(), e);
//        }
//    }
//
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
//
//    @Override
//    public Boolean write(String deviceId, String pointId, String value) {
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
//    }
//
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
