package com.pangu.iot.manager.driver.service.event;

import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.iot.manager.driver.service.IDriverService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverMetadataSyncEvent implements ApplicationListener<DriverEvent> {

    private final IDriverService driverService;
    private final EmqxClient emqxClient;

    @Override
    @SneakyThrows
    public void onApplicationEvent(DriverEvent event) {
        driverService.list().parallelStream().forEach(driver -> {
                String topic = "iot/driver/" + driver.getName() + "/metadata/sync";
                log.info("DriverMetadataSyncEvent topic:{}", topic);
                // iot/driver/pangu-iot-test-driver/metadata/sync
                emqxClient.publish(topic, "update".getBytes(), 2, true);
        });
    }


}
