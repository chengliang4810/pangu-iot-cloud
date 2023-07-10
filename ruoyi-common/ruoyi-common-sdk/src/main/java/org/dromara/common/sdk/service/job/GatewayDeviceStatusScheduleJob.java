package org.dromara.common.sdk.service.job;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.service.GatewayStatusService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 网关设备状态调度任务
 *
 * @author chengliang4810
 * @date 2023/06/24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayDeviceStatusScheduleJob extends QuartzJobBean {

    private final DriverContext driverContext;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final GatewayStatusService gatewayStatusService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<Long, Device> deviceMap = driverContext.getDriverMetadata().getGatewayDeviceMap();
        if (CollUtil.isEmpty(deviceMap)) {
            return;
        }
        deviceMap.keySet().forEach(deviceId -> threadPoolExecutor.execute(() -> gatewayStatusService.status(deviceId)));
    }
}
