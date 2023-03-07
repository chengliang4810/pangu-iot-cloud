package com.pangu.iot.manager;

import cn.hutool.json.JSONUtil;
import com.pangu.iot.manager.driver.service.IDriverSdkService;
import com.pangu.manager.api.domain.DriverMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DriverSdkTest {

    @Autowired
    private IDriverSdkService driverSdkService;


    @Test
    public void sendData(){
        DriverMetadata driverMetadata = driverSdkService.driverMetadataSync("pangu-iot-test-driver");

        System.out.println(JSONUtil.toJsonPrettyStr(driverMetadata));


    }

}
