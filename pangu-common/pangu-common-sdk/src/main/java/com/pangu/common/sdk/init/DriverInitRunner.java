package com.pangu.common.sdk.init;

import com.pangu.common.sdk.bean.DriverProperty;
import com.pangu.common.sdk.service.DriverMetadataService;
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
 */
@Component
@ComponentScan(basePackages = {
        "com.pangu.common.sdk"
})
@EnableConfigurationProperties({DriverProperty.class})
public class DriverInitRunner implements ApplicationRunner {

//    @Resource
//    private DriverCustomService driverCustomService;
    @Resource
    private DriverMetadataService driverMetadataService;
//    @Resource
//    private DriverScheduleService driverScheduleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
         // Initialize driver configuration
         driverMetadataService.initial();

        // Initialize custom driver service
        // driverCustomService.initial();

        // Initialize driver schedule service
        // driverScheduleService.initial();
    }
}
