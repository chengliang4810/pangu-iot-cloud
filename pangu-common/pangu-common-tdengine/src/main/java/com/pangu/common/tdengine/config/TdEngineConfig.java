package com.pangu.common.tdengine.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.pangu.common.tdengine.mapper")
public class TdEngineConfig {
}
