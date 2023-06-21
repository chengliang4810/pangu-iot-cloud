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

import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.constant.driver.ScheduleConstant;
import io.github.pnoker.driver.sdk.entity.property.DriverProperty;
import io.github.pnoker.driver.sdk.entity.property.ScheduleProperty;
import io.github.pnoker.driver.sdk.service.DriverScheduleService;
import io.github.pnoker.driver.sdk.service.job.DriverCustomScheduleJob;
import io.github.pnoker.driver.sdk.service.job.DriverReadScheduleJob;
import io.github.pnoker.driver.sdk.service.job.DriverStatusScheduleJob;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverScheduleServiceImpl implements DriverScheduleService {

    @Resource
    private Scheduler scheduler;
    @Resource
    private DriverProperty driverProperty;

    @Override
    public void initial() {
        ScheduleProperty property = driverProperty.getSchedule();
        if (ObjectUtil.isNull(property)) {
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
