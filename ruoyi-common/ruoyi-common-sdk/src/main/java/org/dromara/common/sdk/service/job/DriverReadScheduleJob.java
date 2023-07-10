package org.dromara.common.sdk.service.job;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.enums.DeviceStatusEnum;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.GatewayStatusContext;
import org.dromara.common.sdk.service.DriverCommandService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Read Schedule Job
 *
 * @author pnoker, chengliang4810
 * @date 2023/06/25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DriverReadScheduleJob extends QuartzJobBean {

    private final DriverContext driverContext;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final DriverCommandService driverCommandService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 网关设备循环
        Map<Long, Device> gatewayDeviceMap = driverContext.getDriverMetadata().getGatewayDeviceMap();
        if (CollUtil.isEmpty(gatewayDeviceMap)) {
            return;
        }

        for (Device gateway : gatewayDeviceMap.values()) {

            DeviceStatusEnum gatewayStatus = GatewayStatusContext.getStatus(gateway.getDeviceId());
            // 网关设备离线，跳过
            if (DeviceStatusEnum.OFFLINE.equals(gatewayStatus)) {
                continue;
            }

            // 网关设备在线，循环下属设备
            List<Device> deviceList = driverContext.getDriverMetadata().getGatewayChildDeviceMap().get(gateway.getDeviceId());
            if (CollUtil.isEmpty(deviceList)) {
                continue;
            }

            // 按照设备循环，读取设备属性点
            for (Device device : deviceList) {
                threadPoolExecutor.execute(() -> {
                    try {
                        driverCommandService.read(device.getDeviceId());
                    } catch (Exception e) {
                        log.error("Read Schedule Job Error: {}", e.getMessage(), e);
                    }
                });
            }

        }


//        Map<Long, Device> deviceMap = driverContext.getDriverMetadata().getDeviceMap();
//        if (ObjectUtil.isNull(deviceMap)) {
//            return;
//        }

//        for (Device device : deviceMap.values()) {
//            Set<String> profileIds = device.getProfileIds();
//            Map<String, Map<String, AttributeInfo>> pointInfoMap = driverContext.getDriverMetadata().getPointInfoMap().get(device.getId());
//            if (CollUtil.isEmpty(profileIds) || ObjectUtil.isNull(pointInfoMap)) {
//                continue;
//            }
//
//            for (String profileId : profileIds) {
//                Map<String, Point> pointMap = driverContext.getDriverMetadata().getProfilePointMap().get(profileId);
//                if (ObjectUtil.isNull(pointMap)) {
//                    continue;
//                }
//
//                for (String pointId : pointMap.keySet()) {
//                    Map<String, AttributeInfo> map = pointInfoMap.get(pointId);
//                    if (ObjectUtil.isNull(map)) {
//                        continue;
//                    }
//
//                    threadPoolExecutor.execute(() -> driverCommandService.read(device.getId(), pointId));
//                }
//            }
//        }
    }
}
