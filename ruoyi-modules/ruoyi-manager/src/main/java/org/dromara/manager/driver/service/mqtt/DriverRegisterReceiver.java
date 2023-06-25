package org.dromara.manager.driver.service.mqtt;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.dromara.common.emqx.annotation.Topic;
import org.dromara.common.emqx.constant.Pattern;
import org.dromara.common.emqx.core.MqttConsumer;
import org.dromara.common.iot.constant.DriverTopic;
import org.dromara.common.iot.dto.DriverSyncUpDTO;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.driver.service.DriverSyncService;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Topic(topic = DriverTopic.DRIVER_REGISTER_SUB_TOPIC, qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}-group")
public class DriverRegisterReceiver extends MqttConsumer<DriverSyncUpDTO> {

    @Autowired
    private DriverSyncService driverSyncService;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic     主题
     * @param entityDTO 实体
     */
    @Override
    protected void messageHandler(String topic, DriverSyncUpDTO entityDTO) {
        if (ObjectUtil.isNull(entityDTO)) {
            log.error("Invalid driver register: {}", entityDTO);
            return;
        }
        driverSyncService.up(entityDTO);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    @Override
    public DriverSyncUpDTO decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DriverSyncUpDTO.class);
    }
}
