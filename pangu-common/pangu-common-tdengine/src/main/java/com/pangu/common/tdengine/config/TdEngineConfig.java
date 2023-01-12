package com.pangu.common.tdengine.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.pangu.common.core.utils.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * td引擎配置
 *
 * @author chengliang4810
 * @date 2023/01/12 15:38
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.druid.tdengine-server.enabled", havingValue = "true")
@MapperScan(basePackages = {"com.pangu.common.tdengine.mapper"}, sqlSessionTemplateRef = "tdengineSqlSessionTemplate")
public class TdEngineConfig {

    @Value("${spring.datasource.druid.tdengine-server.dbName}")
    private String dbName;
    @Value("${spring.datasource.druid.tdengine-server.url}")
    private String jdbc;


    @Bean(name = "tDengineDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.tdengine-server")
    public DataSource tdengineDataSource() {
        return new DruidDataSource();
    }


    @Bean(name = "tDengineSqlSessionFactory")
    public SqlSessionFactory tDengineSqlSessionFactory(@Qualifier("tDengineDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(resolveMapperLocations(StringUtils.split("classpath:mapper/tdengine/*Mapper.xml", ",")));
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }


    @Bean(name = "tdengineSqlSessionTemplate")
    public SqlSessionTemplate tdengineSqlSessionTemplate(@Qualifier("tDengineSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Resource[] resolveMapperLocations(String[] mapperLocations) {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<Resource>();
        if (mapperLocations != null) {
            for (String mapperLocation : mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }


}

