package com.pangu.iot.data;

import cn.hutool.core.collection.ListUtil;
import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import com.pangu.common.tdengine.model.TdColumn;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TdEngineTest {

    @Autowired
    private TdDatabaseMapper tdDatabaseMapper;

    @Autowired
    private TdEngineService tdEngineService;

    @Test
    public void sendData(){
        tdDatabaseMapper.createDB("test");
    }

    @Test
    void createSuperTable() {
        List<TdColumn> columns = ListUtil.of(new TdColumn("ts", "timestamp"), new TdColumn("id", "INT"), new TdColumn("name", "BINARY(20)"));
        List<TdColumn> tags = ListUtil.of(new TdColumn("tag1", "INT"), new TdColumn("tag2", "BINARY(20)"));
        tdEngineService.createSuperTable("test22", columns, tags);
    }



}
