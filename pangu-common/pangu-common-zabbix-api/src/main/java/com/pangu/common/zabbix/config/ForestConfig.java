package com.pangu.common.zabbix.config;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Forest配置
 *
 * @author chengliang
 * @date 2022/11/01
 */
@Configuration
@ForestScan(basePackages = "com.pangu.common.zabbix.api")
@ComponentScan(basePackages = {"com.pangu.common.zabbix.service", "com.pangu.common.zabbix.api", "com.pangu.common.zabbix.interceptor"})
public class ForestConfig {
}
