package org.dromara.modules.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * admin 监控 安全配置
 *
 * @author Lion Li
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfigurer {

    private final String adminContextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        return httpSecurity
            .headers().frameOptions().disable()
            .and().authorizeHttpRequests()
            .requestMatchers(adminContextPath + "/assets/**"
                , adminContextPath + "/login"
                , "/actuator"
                , "/actuator/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage(adminContextPath + "/login")
            .successHandler(successHandler).and()
            .logout().logoutUrl(adminContextPath + "/logout")
            .and()
            .httpBasic().and()
            .csrf()
            .disable()
            .build();
    }

}
