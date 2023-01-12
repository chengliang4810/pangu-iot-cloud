package com.pangu.common.tdengine.init;

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
 * init td引擎数据库
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

    private TdDatabaseMapper commonDao;

    @Override
    public void run(ApplicationArguments args) {

        //先获取TDengine的配置，检测TDengine是否已经配置
        if (containBean(TdEngineConfig.class)) {
            this.dengineConfig = applicationContext.getBean(TdEngineConfig.class);
            this.dataSource = applicationContext.getBean("tDengineDataSource", DruidDataSource.class);
            this.commonDao= applicationContext.getBean(TdDatabaseMapper.class);
            initTDengine(this.dengineConfig.getDbName());
            log.info("使用TDengine存储设备数据，初始化成功");
        }else{
            log.info("使用MySql存储设备数据，初始化成功");
        }
    }

    /**
     * @return
     * @Method
     * @Description 开始初始化加载系统参数, 创建数据库和超级表
     * @Param null
     * @date 2022/5/22,0022 14:27
     * @author wxy
     */
    public void initTDengine(String dbName) {
        try {
            createDatabase();
            //创建数据库表
            commonDao.createSTable(dbName);
            log.info("完成超级表的创建");
        } catch (Exception e) {
            log.error("错误",e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * @Method
     * @Description 根据数据库连接自动创建数据库
     * @Param null
     * @return
     * @date 2022/5/24,0024 14:32
     * @author wxy
     *
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
            System.out.println(newJdbcUrl);

            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
            Connection conn = DriverManager.getConnection(newJdbcUrl, connProps);
            conn.createStatement().execute(String.format("create database  if not exists  %s;",dbName));
            conn.close();
            log.info("完成数据库创建");
        } catch (Exception e) {
            log.info("错误",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return
     * @Method containBean
     * @Description 根据类判断是否有对应bean
     * @author wxy
     */
    private boolean containBean(@Nullable Class<?> T) {
        String[] beans = applicationContext.getBeanNamesForType(T);
        if (beans == null || beans.length == 0) {
            return false;
        } else {
            return true;
        }
    }

}

