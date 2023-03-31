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
 * @author chengliang
 * @date 2023/03/29
 */
@Component
@ComponentScan(basePackages = {
        "com.pangu.common.sdk"
})
@EnableConfigurationProperties({DriverProperty.class})
public class DriverInitRunner implements ApplicationRunner {

    @Resource
    private DriverMetadataService driverMetadataService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
         driverMetadataService.initial();
    }
}
