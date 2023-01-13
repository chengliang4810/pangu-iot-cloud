package com.pangu.iot.data.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.PostgreDialect;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public void paginationInnerInterceptor(@NotNull PaginationInnerInterceptor interceptor){
        interceptor.setDialect(new PostgreDialect());
    }

}
