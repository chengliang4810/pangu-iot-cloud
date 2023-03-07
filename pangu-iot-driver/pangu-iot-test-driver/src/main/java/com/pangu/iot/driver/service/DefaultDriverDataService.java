package com.pangu.iot.driver.service;

import cn.hutool.core.util.RandomUtil;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.manager.api.domain.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultDriverDataService extends DriverDataService {

    @Override
    public DeviceValue read(Device device, List<DeviceAttribute> attributes) {
        return super.read(device, attributes);
    }

    @Override
    public String read(Device device, DeviceAttribute attribute, Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo) {



//        DeviceValue deviceValue = new DeviceValue();
//        deviceValue.setDeviceId("1623590722222985216");
//        deviceValue.setAttributes(Collections.singletonMap("temp", String.valueOf(RandomUtil.randomInt(1, 100))));
//        deviceValue.setClock(System.currentTimeMillis() / 1000);
        return  String.valueOf(RandomUtil.randomInt(1, 100));
    }

    /**
     * 控制设备
     *
     * @param deviceFunction
     */
    @Override
    public Boolean control(DeviceFunction deviceFunction) {
        // 通过异常信息记录日志
        throw new ServiceException("暂不支持控制设备");
        // return true;
    }

}
