package org.dromara.common.sdk.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.common.iot.constant.ScheduleConstant;
import org.dromara.common.sdk.property.DriverProperty;
import org.dromara.common.sdk.property.ScheduleProperty;
import org.dromara.common.sdk.service.DriverScheduleService;
import org.dromara.common.sdk.service.job.DriverCustomScheduleJob;
import org.dromara.common.sdk.service.job.DriverReadScheduleJob;
import org.dromara.common.sdk.service.job.DriverStatusScheduleJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverScheduleServiceImpl implements DriverScheduleService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private DriverProperty driverProperty;

    @Override
    public void initial() {
        ScheduleProperty property = driverProperty.getSchedule();
        if (ObjectUtils.anyNull(property)) {
            return;
        }

        if (Boolean.TRUE.equals(property.getRead().getEnable())) {
            createScheduleJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.READ_SCHEDULE_JOB, property.getRead().getCorn(), DriverReadScheduleJob.class);
        }
        if (Boolean.TRUE.equals(property.getCustom().getEnable())) {
            createScheduleJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.CUSTOM_SCHEDULE_JOB, property.getCustom().getCorn(), DriverCustomScheduleJob.class);
        }
        createScheduleJobWithCorn(ScheduleConstant.DRIVER_SCHEDULE_GROUP, ScheduleConstant.STATUS_SCHEDULE_JOB, ScheduleConstant.DRIVER_STATUS_CORN, DriverStatusScheduleJob.class);

        try {
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("Driver schedule initial error: {}", e.getMessage(), e);
        }
    }

    /**
     * 创建调度任务
     *
     * @param group    group
     * @param name     name
     * @param corn     corn
     * @param jobClass class
     */
    @SneakyThrows
    public void createScheduleJobWithCorn(String group, String name, String corn, Class<? extends Job> jobClass) {
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(name, group)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(corn))
                .startNow().build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

}
