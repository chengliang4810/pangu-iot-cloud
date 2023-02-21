package com.pangu.common.sdk.mqtt;

import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@RequiredArgsConstructor
@Topic(topic = "iot/device/#/function/#/exec", qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class FunctionExecConsumer extends MqttConsumer<DeviceFunction> {

    @Resource
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
        // 捕捉异常，记录日志
        try {
            System.out.println("控制设备: " + deviceFunction);
            driverDataService.control(deviceFunction);
        } catch (Exception e){
            log.error("控制设备失败: ", e);
        }
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
