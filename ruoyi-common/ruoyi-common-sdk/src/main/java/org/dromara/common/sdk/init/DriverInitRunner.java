package org.dromara.common.sdk.init;

import org.dromara.common.sdk.property.DriverProperty;
import org.dromara.common.sdk.service.DriverCustomService;
import org.dromara.common.sdk.service.DriverScheduleService;
import org.dromara.common.sdk.service.DriverSyncService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Driver SDK Initial
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Component
@ComponentScan(basePackages = {
        "org.dromara.common.sdk",
})
@EnableConfigurationProperties({DriverProperty.class})
public class DriverInitRunner implements ApplicationRunner {

    @Resource
    private DriverSyncService driverSyncService;
    @Resource
    private DriverCustomService driverCustomService;
    @Resource
    private DriverScheduleService driverScheduleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 驱动同步
        driverSyncService.up();

        // 执行驱动模块的自定义初始化函数
        driverCustomService.initial();

        // 初始化驱动任务，包括驱动状态、读和自定义任务
        driverScheduleService.initial();
    }
}
