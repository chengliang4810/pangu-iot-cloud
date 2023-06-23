package org.dromara.common.sdk.service.job;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.service.DriverCustomService;
import org.dromara.common.sdk.service.DriverSenderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 网关设备状态调度任务
 *
 * @author chengliang4810
 * @date 2023/06/24
 */
@Slf4j
@Component
public class GatewayDeviceStatusScheduleJob extends QuartzJobBean {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;
    @Resource
    private DriverCustomService driverCustomService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//         DriverEventDTO.DriverStatus driverStatus = new DriverEventDTO.DriverStatus(driverContext.getDriverMetadata().getDriverId(), driverContext.getDriverStatus());
//         log.debug("Send driver status event: {}", JsonUtils.toJsonString(driverStatus));
//         driverSenderService.driverStatusSender(driverStatus);
//         threadPoolExecutor.execute(() -> driverCustomService.gatewayStatus());
    }
}
