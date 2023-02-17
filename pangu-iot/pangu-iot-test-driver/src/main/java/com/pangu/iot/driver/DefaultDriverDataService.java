package com.pangu.iot.driver;

import cn.hutool.core.util.RandomUtil;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceValue;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DefaultDriverDataService implements DriverDataService {

    @Override
    public List<DeviceValue> read() {
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.setDeviceId("1623590722222985216");
        deviceValue.setAttributes(Collections.singletonMap("temp", String.valueOf(RandomUtil.randomInt(1, 100))));
        deviceValue.setClock(System.currentTimeMillis() / 1000);
        return Collections.singletonList(deviceValue);
    }

    /**
     * 控制设备
     */
    @Override
    public void control() {

    }


}
