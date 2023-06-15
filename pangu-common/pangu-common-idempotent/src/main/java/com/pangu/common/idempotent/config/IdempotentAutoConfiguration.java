package com.pangu.common.idempotent.config;

import com.pangu.common.idempotent.aspectj.RepeatSubmitAspect;
import com.pangu.common.redis.config.RedisConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 幂等功能配置
 *
 * @author Lion Li
 */
@AutoConfiguration(after = RedisConfiguration.class)
public class IdempotentAutoConfiguration {

	@Bean
	public RepeatSubmitAspect repeatSubmitAspect() {
		return new RepeatSubmitAspect();
	}

}
