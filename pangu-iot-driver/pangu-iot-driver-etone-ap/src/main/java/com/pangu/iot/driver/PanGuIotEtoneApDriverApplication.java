package com.pangu.iot.driver;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 奕通AP定位模块
 *
 * @author chengliang4810
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PanGuIotEtoneApDriverApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PanGuIotEtoneApDriverApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  IOT奕通定位驱动模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
