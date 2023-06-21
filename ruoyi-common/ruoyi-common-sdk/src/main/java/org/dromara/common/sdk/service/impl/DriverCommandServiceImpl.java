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

package org.dromara.common.sdk.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.common.DefaultConstant;
import io.github.pnoker.common.dto.DeviceCommandDTO;
import io.github.pnoker.common.entity.driver.AttributeInfo;
import io.github.pnoker.common.entity.point.PointValue;
import io.github.pnoker.common.enums.AttributeTypeFlagEnum;
import io.github.pnoker.common.exception.ReadPointException;
import io.github.pnoker.common.exception.ServiceException;
import io.github.pnoker.common.model.Device;
import io.github.pnoker.common.model.Point;
import io.github.pnoker.common.utils.JsonUtil;
import io.github.pnoker.driver.sdk.DriverContext;
import io.github.pnoker.driver.sdk.service.DriverCommandService;
import io.github.pnoker.driver.sdk.service.DriverCustomService;
import io.github.pnoker.driver.sdk.service.DriverSenderService;
import io.github.pnoker.driver.sdk.utils.ConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverCommandServiceImpl implements DriverCommandService {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;
    @Resource
    private DriverCustomService driverCustomService;

    @Override
    public PointValue read(String deviceId, String pointId) {
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
                throw new ReadPointException("The read point value is null");
            }
            if (DefaultConstant.DEFAULT_VALUE.equals(rawValue)) {
                throw new ReadPointException(CharSequenceUtil.format("The read point value is invalid: {}", rawValue));
            }

            PointValue pointValue = new PointValue(deviceId, pointId, rawValue, ConvertUtil.convertValue(point, rawValue));
            driverSenderService.pointValueSender(pointValue);
            return pointValue;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void read(DeviceCommandDTO commandDTO) {
        DeviceCommandDTO.DeviceRead deviceRead = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceRead.class);
        if (ObjectUtil.isNull(deviceRead)) {
            return;
        }

        log.info("Start command of read: {}", JsonUtil.toPrettyJsonString(commandDTO));
        PointValue read = read(deviceRead.getDeviceId(), deviceRead.getPointId());
        log.info("End command of read: {}", JsonUtil.toPrettyJsonString(read));
    }

    @Override
    public Boolean write(String deviceId, String pointId, String value) {
        Device device = driverContext.getDeviceByDeviceId(deviceId);
        try {
            Point point = driverContext.getPointByDeviceIdAndPointId(deviceId, pointId);
            AttributeTypeFlagEnum typeEnum = AttributeTypeFlagEnum.ofCode(point.getPointTypeFlag().getCode());
            return driverCustomService.write(
                    driverContext.getDriverInfoByDeviceId(deviceId),
                    driverContext.getPointInfoByDeviceIdAndPointId(deviceId, pointId),
                    device,
                    new AttributeInfo(value, typeEnum)
            );
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void write(DeviceCommandDTO commandDTO) {
        DeviceCommandDTO.DeviceWrite deviceWrite = JsonUtil.parseObject(commandDTO.getContent(), DeviceCommandDTO.DeviceWrite.class);
        if (ObjectUtil.isNull(deviceWrite)) {
            return;
        }

        log.info("Start command of write: {}", JsonUtil.toPrettyJsonString(commandDTO));
        Boolean write = write(deviceWrite.getDeviceId(), deviceWrite.getPointId(), deviceWrite.getValue());
        log.info("End command of write: write {}", write);
    }

}
