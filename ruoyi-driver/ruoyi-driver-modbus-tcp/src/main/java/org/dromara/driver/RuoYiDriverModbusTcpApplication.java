package org.dromara.driver;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * ModbusTcp驱动模块
 *
 * @author chengliang4810
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RuoYiDriverModbusTcpApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RuoYiDriverModbusTcpApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  ModbusTcp驱动模块启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
