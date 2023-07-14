package org.dromara.manager.data.service.mqtt;

import cn.hutool.core.collection.CollUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.dromara.common.emqx.annotation.Topic;
import org.dromara.common.emqx.constant.Pattern;
import org.dromara.common.emqx.core.MqttConsumer;
import org.dromara.common.iot.constant.DeviceAttributeTopic;
import org.dromara.common.iot.entity.device.DeviceValue;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.data.context.DataHandlerContext;
import org.dromara.manager.data.dto.DeviceAttributeValue;
import org.eclipse.paho.mqttv5.common.MqttMessage;

/**
 * 设备值接收机
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Slf4j
@RequiredArgsConstructor
@Topic(topic = DeviceAttributeTopic.DEVICE_STATUS_SUB_TOPIC, patten = Pattern.SHARE, group = "${spring.application.name}-group")
public class DeviceValueReceiver extends MqttConsumer<DeviceValue> {

    private final FlowExecutor flowExecutor;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic     主题
     * @param deviceValue 实体
     */
    @Override
    protected void messageHandler(String topic, DeviceValue deviceValue) throws Exception {
        if (deviceValue == null || CollUtil.isEmpty(deviceValue.getAttributes())) {
            return;
        }
        log.debug("messageHandler deviceValue: {}", deviceValue);
        deviceValue.getAttributes().forEach((key, value) -> {
            DeviceAttributeValue deviceAttributeValue = new DeviceAttributeValue(deviceValue.getDeviceCode(), key, value);
            try {
                LiteflowResponse response = flowExecutor.execute2Resp("dataHandlerChain", deviceAttributeValue, DataHandlerContext.class);
                log.debug("response success: {}", response.isSuccess());
            } catch (Exception e) {
                log.error("error: {}", e.getMessage(), e);
            }
        });
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
