package org.dromara.common.sdk.service.job;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.dto.DriverEventDTO;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.service.DriverSenderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 驱动状态调度任务
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverStatusScheduleJob extends QuartzJobBean {

    @Resource
    private DriverContext driverContext;
    @Resource
    private DriverSenderService driverSenderService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
         DriverEventDTO.DriverStatus driverStatus = new DriverEventDTO.DriverStatus(driverContext.getDriverMetadata().getDriverId(), driverContext.getDriverStatus());
         log.debug("Send driver status event: {}", JsonUtils.toJsonString(driverStatus));
         driverSenderService.driverStatusSender(driverStatus);
    }
}
