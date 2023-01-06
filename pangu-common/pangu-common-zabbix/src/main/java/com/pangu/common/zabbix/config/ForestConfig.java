package com.pangu.common.zabbix.config;

import com.dtflys.forest.springboot.annotation.ForestScan;
import org.springframework.context.annotation.Configuration;

/**
 * Forest配置
 *
 * @author chengliang
 * @date 2022/11/01
 */
@Configuration
@ForestScan(basePackages = "com.pangu.common.zabbix.api")
public class ForestConfig {
}
