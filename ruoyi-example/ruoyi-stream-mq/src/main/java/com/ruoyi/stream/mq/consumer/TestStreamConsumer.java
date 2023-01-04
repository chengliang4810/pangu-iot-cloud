package com.ruoyi.stream.mq.consumer;

import com.ruoyi.stream.mq.TestMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class TestStreamConsumer {

    @Bean
    Consumer<TestMessaging> demo() {
        log.info("初始化订阅");
        return msg -> {
            log.info("通过stream消费到消息 => {}", msg.toString());
        };
    }

}
