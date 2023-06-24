package org.dromara.common.sdk.service.job;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.sdk.service.DriverCustomService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 自定义调度任务
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverCustomScheduleJob extends QuartzJobBean {

    @Autowired
    private DriverCustomService driverCustomService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        driverCustomService.schedule();
    }
}
