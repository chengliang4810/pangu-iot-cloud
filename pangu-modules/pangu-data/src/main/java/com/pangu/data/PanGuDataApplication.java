package com.pangu.data;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * IOT数据模块
 *
 * @author chengliang4810
 */
@EnableDubbo
@SpringBootApplication
public class PanGuDataApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PanGuDataApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  IOT数据模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
