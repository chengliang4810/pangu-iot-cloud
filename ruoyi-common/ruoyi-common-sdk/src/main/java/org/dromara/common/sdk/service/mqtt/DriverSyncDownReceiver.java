package org.dromara.common.sdk.service.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.dromara.common.core.constant.SymbolConstants;
import org.dromara.common.emqx.annotation.Topic;
import org.dromara.common.emqx.core.MqttConsumer;
import org.dromara.common.iot.constant.DriverTopic;
import org.dromara.common.iot.dto.DriverSyncDownDTO;
import org.dromara.common.iot.enums.DriverStatusEnum;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.utils.AddressUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Topic(qos = 2)
public class DriverSyncDownReceiver extends MqttConsumer<DriverSyncDownDTO> {

    @Value("${server.port}")
    private Integer servicePort;
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private DriverContext driverContext;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param entity 实体
     */
    @Override
    protected void messageHandler(String topic, DriverSyncDownDTO entity) {
        System.out.println("driver register back....." + driverContext);
        driverContext.setDriverStatus(DriverStatusEnum.ONLINE);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link T}
     */
    @Override
    public DriverSyncDownDTO decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DriverSyncDownDTO.class);
    }

    /**
     * 订阅主题
     *
     * @return {@link String}
     */
    @Override
    public String getTopic() {
        String driverUniqueKey = applicationName + SymbolConstants.UNDERSCORE + AddressUtils.localHost() + SymbolConstants.UNDERSCORE + servicePort;
        return DriverTopic.getDriverRegisterBackTopic(driverUniqueKey);
    }

}
