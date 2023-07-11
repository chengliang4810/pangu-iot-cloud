package org.dromara.data.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.dromara.common.emqx.annotation.Topic;
import org.dromara.common.emqx.constant.Pattern;
import org.dromara.common.emqx.core.MqttConsumer;
import org.dromara.common.iot.constant.DeviceAttributeTopic;
import org.dromara.common.iot.entity.device.DeviceValue;
import org.dromara.common.json.utils.JsonUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;

@Slf4j
@Topic(topic = DeviceAttributeTopic.DEVICE_STATUS_SUB_TOPIC, qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}-group")
public class DriverRegisterReceiver extends MqttConsumer<DeviceValue> {

//    @Autowired
//    private DriverSyncService driverSyncService;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic     主题
     * @param deviceValue 实体
     */
    @Override
    protected void messageHandler(String topic, DeviceValue deviceValue) throws Exception {
//        if (ObjectUtil.isNull(entityDTO)) {
//            log.error("Invalid driver register: {}", entityDTO);
//            return;
//        }
//        driverSyncService.up(entityDTO);
        log.info("deviceValue: {}", deviceValue);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    @Override
    public DeviceValue decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DeviceValue.class);
    }
}
