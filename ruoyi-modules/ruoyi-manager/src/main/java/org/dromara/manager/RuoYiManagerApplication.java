package org.dromara.manager;

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
public class RuoYiManagerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiManagerApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  IOT管理模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
