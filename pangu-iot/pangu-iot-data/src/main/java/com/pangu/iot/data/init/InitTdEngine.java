package com.pangu.iot.data.init;

import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * init td引擎
 *
 * @author chengliang4810
 * @date 2023/01/13 10:14
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitTdEngine implements ApplicationRunner {

    private final TdDatabaseMapper databaseMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //创建数据库
        int result = databaseMapper.createDB("pangu_iot");
        log.info("创建数据库结果：{}", result);
    }


}
