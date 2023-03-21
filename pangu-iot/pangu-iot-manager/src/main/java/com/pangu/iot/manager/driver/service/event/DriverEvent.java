package com.pangu.iot.manager.driver.service.event;

import org.springframework.context.ApplicationEvent;

public class DriverEvent extends ApplicationEvent {

    public DriverEvent(Object source) {
        super(source);
    }

}
