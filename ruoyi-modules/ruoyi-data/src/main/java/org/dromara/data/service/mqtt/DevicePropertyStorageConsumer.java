package org.dromara.data.service.mqtt;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.dromara.common.emqx.annotation.Topic;
import org.dromara.common.emqx.constant.Pattern;
import org.dromara.common.emqx.core.MqttConsumer;
import org.dromara.common.iot.dto.StoreValueDTO;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.data.service.DataService;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Component;

import static org.dromara.common.iot.constant.StorageTopic.STORAGE_DEVICE_PROPERTY_SUB_TOPIC;

@Slf4j
@Component
@Topic(topic = STORAGE_DEVICE_PROPERTY_SUB_TOPIC, patten = Pattern.SHARE, group = "${spring.application.name}-group")
public class DevicePropertyStorageConsumer extends MqttConsumer<StoreValueDTO> {

    @Resource
    private DataService dataService;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param entity 实体
     */
    @Override
    protected void messageHandler(String topic, StoreValueDTO entity) throws Exception {
        dataService.storageTdEngine(entity);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    @Override
    public StoreValueDTO decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), StoreValueDTO.class);
    }
}
