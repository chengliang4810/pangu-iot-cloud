package com.pangu.common.tdengine.init;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.pangu.common.tdengine.config.TdEngineConfig;
import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 初始化 td引擎数据库
 *
 * @author chengliang4810
 * @date 2023/01/12 15:41
 */
@Slf4j
@Component
public class InitTdEngineDB  implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;

    private DruidDataSource dataSource;

    private TdEngineConfig dengineConfig;

    private TdDatabaseMapper tdDatabaseMapper;

    @Override
    public void run(ApplicationArguments args) {
        //先获取TDengine的配置，检测TDengine是否已经配置
        if (containBean(TdEngineConfig.class)) {
            this.dengineConfig = applicationContext.getBean(TdEngineConfig.class);
            this.dataSource = applicationContext.getBean("tDengineDataSource", DruidDataSource.class);
            this.tdDatabaseMapper= applicationContext.getBean(TdDatabaseMapper.class);
            initTdEngine(this.dengineConfig.getDbName());
            log.info("TDengine configuration initialization success");
        }else{
            log.warn("TDengine configuration initialization failed");
        }
    }

    /**
     * 开始初始化加载系统参数, 创建数据库和超级表
     */
    public void initTdEngine(String dbName) {
        try {
            createDatabase();
            //创建数据库表
            // tdDatabaseMapper.createSuperTable(dbName);
            log.info("super table [{}] is created", dbName);
        } catch (Exception e) {
            log.error("failed to create the super table : {}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据数据库连接自动创建数据库
     */
    private void createDatabase(){
        try {
            String dbName = dengineConfig.getDbName();
            String jdbcUrl = dataSource.getRawJdbcUrl();
            String username = dataSource.getUsername();
            String password = dataSource.getPassword();
            jdbcUrl += ("&user=" + username);
            jdbcUrl += ("&password=" + password);
            int startIndex = jdbcUrl.indexOf('/',12);
            int endIndex = jdbcUrl.indexOf('?');
            String newJdbcUrl = jdbcUrl.substring(0,startIndex);
            newJdbcUrl= newJdbcUrl+jdbcUrl.substring(endIndex);

            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
            Connection conn = DriverManager.getConnection(newJdbcUrl, connProps);
            conn.createStatement().execute(String.format("create database  if not exists  %s;",dbName));
            conn.close();
            log.info("database [{}] created successfully", dbName);
        } catch (Exception e) {
            log.info("database created fail: {}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据类判断是否有对应bean
     */
    private boolean containBean(@Nullable Class<?> T) {
        String[] beans = applicationContext.getBeanNamesForType(T);
        if (ObjectUtil.isNull(beans) || beans.length == 0) {
            return false;
        } else {
            return true;
        }
    }

}

