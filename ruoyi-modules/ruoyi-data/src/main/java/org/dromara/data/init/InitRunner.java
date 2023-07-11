package org.dromara.data.init;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.taosdata.jdbc.TSDBDriver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.SpringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initDatabase();
    }


    /**
     * 根据数据库连接自动创建数据库
     */
    private void initDatabase(){
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringUtils.getBean(DynamicRoutingDataSource.class);
        DataSource masterDataSource = dynamicRoutingDataSource.getDataSource("master");
        ItemDataSource itemDataSource = (ItemDataSource) masterDataSource;
        HikariDataSource dataSource = (HikariDataSource) itemDataSource.getRealDataSource();
        System.out.println(dataSource.getJdbcUrl());
        try {
            String jdbcUrl = dataSource.getJdbcUrl();
            String username = dataSource.getUsername();
            String password = dataSource.getPassword();
            jdbcUrl += ("&user=" + username);
            jdbcUrl += ("&password=" + password);
            int startIndex = jdbcUrl.indexOf('/',12);
            int endIndex = jdbcUrl.indexOf('?');
            String newJdbcUrl = jdbcUrl.substring(0,startIndex);
            newJdbcUrl= newJdbcUrl+jdbcUrl.substring(endIndex);
            System.out.println(jdbcUrl);

            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");
            Connection conn = DriverManager.getConnection(newJdbcUrl, connProps);
            // 通过jdbcUrl 截取数据库名称,执行创建数据库语句
            String databaseName = getDatabaseName(jdbcUrl);
            conn.createStatement().execute(String.format("create database  if not exists  %s;", databaseName));
            conn.close();
            log.info("初始化数据库成功: {}", databaseName);
        } catch (Exception e) {
            log.info("初始化数据库错误: {}",e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 解析数据库名称
     *
     * @param jdbcUrl jdbc url
     * @return {@link String}
     */
    private String getDatabaseName(String jdbcUrl){
        // 是否存在? 参数
        int indexOf = jdbcUrl.lastIndexOf("?");
        if (indexOf != -1) {
            jdbcUrl = jdbcUrl.substring(0, indexOf);
        }
        List<String> stringList = StrUtil.split(jdbcUrl, "/");
        return stringList.get(stringList.size() - 1);
    }

}
