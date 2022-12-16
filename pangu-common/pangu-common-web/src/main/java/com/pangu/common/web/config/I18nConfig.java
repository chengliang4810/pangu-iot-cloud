package com.pangu.common.web.config;

import com.pangu.common.web.core.I18nLocaleResolver;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 国际化配置
 *
 * @author chengliang4810
 */
@AutoConfiguration(before = WebMvcAutoConfiguration.class)
public class I18nConfig {

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nLocaleResolver();
    }

}
