package com.pangu.iot.manager.driver.service.mqtt;

import cn.hutool.core.util.BooleanUtil;
import com.pangu.common.core.domain.dto.DeviceExecuteResult;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 设备执行回调函数
 *
 * @author chengliang
 * @date 2023/03/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Topic(topic = "iot/device/+/function/+/exec/callback",qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class DeviceFunctionExecuteCallback extends MqttConsumer<DeviceExecuteResult> {

    private final IServiceExecuteRecordService serviceExecuteRecordService;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param result 实体
     */
    @Override
    protected void messageHandler(String topic, DeviceExecuteResult result) {
        Boolean success = result.getSuccess();
        if (BooleanUtil.isTrue(success)) {
            RedisUtils.setCacheObject("iot:device:" + result.getDeviceId() + ":function:" + result.getIdentifier() + ":" + result.getUuid(), "success", Duration.ofSeconds(10));
        } else {
            RedisUtils.setCacheObject("iot:device:" + result.getDeviceId() + ":function:" + result.getIdentifier() + ":" + result.getUuid(), "failure", Duration.ofSeconds(10));
        }

        // 更新日志结果
        serviceExecuteRecordService.updateById(new ServiceExecuteRecord(result.getUuid(), result.getSuccess()));
        log.info("设备执行回调函数: {}", result);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    @Override
    public DeviceExecuteResult decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DeviceExecuteResult.class);
    }
}
