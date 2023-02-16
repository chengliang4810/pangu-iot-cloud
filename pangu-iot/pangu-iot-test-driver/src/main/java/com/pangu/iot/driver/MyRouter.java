package com.pangu.iot.driver;

import com.pangu.common.sdk.camel.ZabbixRouter;
import com.pangu.common.zabbix.model.DeviceValue;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MyRouter extends ZabbixRouter {


    @Override
    public List<DeviceValue> read() {
        return Collections.emptyList();
    }


}
