package com.pangu.iot.data.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
//    @Autowired
//    public void mybatisPlusInterceptor(MybatisPlusInterceptor interceptor) {
//        // 添加自动分页插件
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
//        System.out.println("MybatisPlusConfig: mybatisPlusInterceptor");
//    }


}
