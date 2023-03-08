package com.pangu.iot.manager.driver.service.event;

import com.pangu.iot.manager.driver.service.IDriverService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverMetadataSyncEvent implements ApplicationListener<DriverEvent> {

    private final IDriverService driverService;
    private final MqttClient mqttClient;

    @Override
    @SneakyThrows
    public void onApplicationEvent(DriverEvent event) {
        driverService.list().parallelStream().forEach(driver -> {
            try {
                mqttClient.publish("pangu/iot/driver/" + driver.getName() + "/metadata/sync", "1".getBytes(), 0, false);
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
