package com.pangu.iot.manager.alarm.service.impl.mqtt;

import cn.hutool.core.util.NumberUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.domain.dto.HostsDTO;
import com.pangu.common.core.domain.dto.MqttMessageDTO;
import com.pangu.common.core.domain.dto.TagsDTO;
import com.pangu.common.core.domain.dto.ZabbixEventDTO;
import com.pangu.common.core.enums.MqttMessageType;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.emqx.utils.EmqxUtil;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.manager.alarm.service.IProblemService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.service.IProductEventService;
import com.pangu.manager.api.domain.dto.AlarmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 告警消费服务
 * 告警从Data模块发送过来，Data模块通过MQTT协议发送到EMQX，EMQX再通过MQTT协议发送到Manager模块
 * @author chengliang
 * @date 2023/03/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Topic(topic = "server/device/+/problem/+", qos = 2 , patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class ProblemConsumerService extends MqttConsumer<ZabbixEventDTO> {

    private final IDeviceService deviceService;
    private final IProblemService problemService;
    private final IProductEventService productEventService;

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link ZabbixEventDTO}
     */
    @Override
    public ZabbixEventDTO decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), ZabbixEventDTO.class);
    }

    /**
     * 消息处理程序,业务操作
     *
     * @param topic       主题
     * @param zabbixEvent 告警事件
     */
    @Override
    protected void messageHandler(String topic, ZabbixEventDTO zabbixEvent) {
        List<HostsDTO> zabbixEventHosts = zabbixEvent.getHosts();
        Map<String, String> tagMap = zabbixEvent.getTags().stream().collect(Collectors.toMap(TagsDTO::getTag, TagsDTO::getValue));
        zabbixEventHosts.forEach(hostsDTO -> {
            String eventRuleId = zabbixEvent.getName();
            String deviceId = hostsDTO.getName();

            log.debug("设备告警：{}", zabbixEvent);

            // 告警记录
            this.problemService.addAlarmRecord(new AlarmDTO(Long.valueOf(eventRuleId), zabbixEvent.getSeverity(), Long.valueOf(deviceId), zabbixEvent.getEventId().longValue(), 0L, TimeUtil.toLocalDateTime(zabbixEvent.getClock())));
            ProductEvent productEvent = this.productEventService.getById(eventRuleId);
            if (productEvent == null) {
                log.warn("告警事件不存在：{}", zabbixEvent);
                return;
            }

            // 发送到EMQX
            MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(MqttMessageType.ALARM, deviceId, zabbixEvent.getSeverity(), productEvent.getName());
            EmqxUtil.getClient().publish("iot/device/" + deviceId + "/problem/" + zabbixEvent.getSeverity(), JsonUtils.toJsonString(mqttMessageDTO));
            if (tagMap.containsKey(IotConstants.EXECUTE_TAG_NAME)) {
                // 调用设备执行
                log.info("设备服务：{}", zabbixEvent);
                // deviceServiceExecute(zbxProblem);
            }

        });
    }

    /**
     * 设备功能执行
     */
    private void deviceServiceExecute(ZbxProblem zbxProblem) {
        String eventRuleId = zbxProblem.getName();
        String deviceId = zbxProblem.getHostname();
        // 执行功能
        this.deviceService.executeService(NumberUtil.parseLong(deviceId), NumberUtil.parseLong(eventRuleId), 1);
        // 设备功能
        log.info("设备服务：{}", zbxProblem);
    }


}
