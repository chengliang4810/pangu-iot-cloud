package com.pangu.iot.data;

import com.pangu.common.zabbix.model.ZbxItemValue;
import com.pangu.common.zabbix.service.ZbxDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeviceValueTest {

    @Autowired
    private ZbxDataService dataService;

    @Test
    public void sendData(){
        ZbxItemValue itemValue = new ZbxItemValue();
        itemValue.setValue("100");
        itemValue.setKey("temp");
        itemValue.setHost("device-1");
        itemValue.setClock(System.currentTimeMillis() / 1000);
        dataService.sendData(itemValue);
    }

}
