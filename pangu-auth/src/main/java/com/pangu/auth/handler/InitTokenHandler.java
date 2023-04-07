package com.pangu.auth.handler;

import com.pangu.auth.service.SysLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * init Token 处理程序
 *
 * @author chengliang
 * @date 2023/04/07
 */
@Slf4j
@Order(999)
@Configuration
public class InitTokenHandler implements ApplicationRunner {

    @Lazy
    @Resource
    private SysLoginService sysLoginService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // sysLoginService.registerAllApiToken();
        log.info("init Token success");
    }

}
