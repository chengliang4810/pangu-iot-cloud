package com.pangu.iot.manager.driver.service.mqtt;

import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.manager.api.domain.dto.DriverStatus;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Topic(topic = IotConstants.Topic.Driver.DRIVER_TOPIC_HEARTBEAT_SUBSCRIBE_TOPIC, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class DriverHeartbeatReceiver extends MqttConsumer<DriverStatus> {


    @Override
    protected void messageHandler(String topic, DriverStatus entity) {
        log.info("收到驱动心跳: {}", entity);
        RedisUtils.setCacheObject(IotConstants.RedisKey.DRIVER_HEARTBEAT + entity.getPrimaryDriverName(), LocalDateTime.now());
    }

    @Override
    public DriverStatus decoder(MqttMessage message) {
        byte[] payload = message.getPayload();
        return JsonUtils.parseObject(payload, DriverStatus.class);
    }

}
