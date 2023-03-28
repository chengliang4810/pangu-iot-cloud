package com.pangu.common.zabbix.service;

import com.google.gson.Gson;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.api.ZbxSenderService;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.common.zabbix.model.ZabbixTrapper;
import com.pangu.common.zabbix.model.ZbxItemValue;
import com.pangu.common.zabbix.model.ZbxResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * zabbix数据服务 通过消息队列发送数据到zabbix
 *
 * @author chengliang
 * @date 2023/02/15 11:09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SenderDataService {

    private final ZbxSenderService senderService;

    private Gson gson = new Gson();

    /**
     * 直连zabbix发送数据，支持批量发送与指定数据时间
     *
     * @param itemValue 值
     */
    public ZbxResponse sendData(List<ZbxItemValue> itemValue) {
        Assert.notEmpty(itemValue, "itemValue is null");
        log.debug("send data to zabbix: {}", itemValue);
        try {
            ZabbixTrapper zabbixTrapper = new ZabbixTrapper(itemValue);
            return senderService.sendData(gson.toJson(zabbixTrapper));
        } catch (IOException e) {
            log.error("send data to zabbix error: {}", e.getMessage());
            throw new ServiceException("send data to zabbix error: " + e.getMessage());
        }
    }

    /**
     * 直连zabbix发送数据，支持指定数据时间
     *
     * @param itemValue 值
     */
    public ZbxResponse sendData(ZbxItemValue itemValue) {
       return this.sendData(Collections.singletonList(itemValue));
    }


    /**
     * 发送数据到zabbix
     *
     * @param deviceValue 设备数据
     */
    public ZbxResponse sendData(DeviceValue deviceValue) {
        Assert.notNull(deviceValue, "deviceValue is null");
        Assert.notNull(deviceValue.getDeviceId(), "deviceId is null");
        Assert.notNull(deviceValue.getAttributes(), "device attribute is null");

        List<ZbxItemValue> itemValues = new ArrayList<>(deviceValue.getAttributes().size());

        deviceValue.getAttributes().forEach((key,value) -> {
            ZbxItemValue itemValue = new ZbxItemValue();
            itemValue.setHost(deviceValue.getDeviceId());
            itemValue.setKey(key);
            itemValue.setValue(value);
            itemValue.setClock(deviceValue.getClock());
            itemValue.setNs(0L);
            itemValues.add(itemValue);
        });

        return this.sendData(itemValues);
    }
}
