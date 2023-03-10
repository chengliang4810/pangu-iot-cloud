package com.pangu.iot.driver;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * IOT管理模块
 *
 * @author chengliang4810
 */
@EnableDubbo
@SpringBootApplication
public class PanGuIotTestDriverApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PanGuIotTestDriverApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  IOT测试驱动模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
