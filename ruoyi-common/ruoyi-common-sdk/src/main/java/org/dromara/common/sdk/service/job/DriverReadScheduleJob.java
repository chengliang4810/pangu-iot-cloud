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

package org.dromara.common.sdk.service.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.pnoker.common.entity.driver.AttributeInfo;
import io.github.pnoker.common.model.Device;
import io.github.pnoker.common.model.Point;
import io.github.pnoker.driver.sdk.DriverContext;
import io.github.pnoker.driver.sdk.service.DriverCommandService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Read Schedule Job
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Component
public class DriverReadScheduleJob extends QuartzJobBean {

    @Resource
    private DriverContext driverContext;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource
    private DriverCommandService driverCommandService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Map<String, Device> deviceMap = driverContext.getDriverMetadata().getDeviceMap();
        if (ObjectUtil.isNull(deviceMap)) {
            return;
        }

        for (Device device : deviceMap.values()) {
            Set<String> profileIds = device.getProfileIds();
            Map<String, Map<String, AttributeInfo>> pointInfoMap = driverContext.getDriverMetadata().getPointInfoMap().get(device.getId());
            if (CollUtil.isEmpty(profileIds) || ObjectUtil.isNull(pointInfoMap)) {
                continue;
            }

            for (String profileId : profileIds) {
                Map<String, Point> pointMap = driverContext.getDriverMetadata().getProfilePointMap().get(profileId);
                if (ObjectUtil.isNull(pointMap)) {
                    continue;
                }

                for (String pointId : pointMap.keySet()) {
                    Map<String, AttributeInfo> map = pointInfoMap.get(pointId);
                    if (ObjectUtil.isNull(map)) {
                        continue;
                    }

                    threadPoolExecutor.execute(() -> driverCommandService.read(device.getId(), pointId));
                }
            }
        }
    }
}
