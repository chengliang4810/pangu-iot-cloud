package com.pangu.iot.data;

import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TdEngineTest {

    @Autowired
    private TdDatabaseMapper tdDatabaseMapper;

    @Test
    public void sendData(){
        tdDatabaseMapper.createDB("test");
    }

}
