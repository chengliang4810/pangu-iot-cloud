package com.pangu.common.sdk.mqtt;

import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Topic(topic = "iot/device/#/function/#/exec", qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class FunctionExecConsumer extends MqttConsumer<DeviceFunction> {

    private final DriverContext driverContext;
    private final DriverDataService driverDataService;


    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param deviceFunction 实体
     */
    @Override
    protected void messageHandler(String topic, DeviceFunction deviceFunction) {
        // 设备是否属于该驱动， 属于则调用执行功能方法
        if (driverContext.getDriverMetadata().getDeviceMap().containsKey(deviceFunction.getDeviceId())) {
            // 捕捉异常，记录日志
            try {
                driverDataService.control(deviceFunction);
            } catch (Exception e){
                log.error("控制设备失败: ", e);
            }
            return;
        }
        log.warn("设备不属于该驱动管理: {}", deviceFunction.getDeviceId());
    }

    /**
     * 解码器
     *
     * @param message 消息
     */
    @Override
    public DeviceFunction decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DeviceFunction.class);
    }


}
