package com.pangu.iot.manager.zabbix;

import cn.hutool.core.util.NumberUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.domain.dto.MqttMessageDTO;
import com.pangu.common.core.domain.dto.Tag;
import com.pangu.common.core.enums.MqttMessageType;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.common.zabbix.domain.ProblemMessage;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.service.ReceiveProblemService;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.data.api.RemoteDeviceStatusService;
import com.pangu.iot.manager.alarm.service.IProblemService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.service.IProductEventService;
import com.pangu.manager.api.domain.dto.AlarmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemConsumerService implements ReceiveProblemService {

    private final EmqxClient emqxClient;
    private final IDeviceService deviceService;
    private final IProblemService problemService;
    private final IProductEventService productEventService;
    private final RemoteDeviceStatusService deviceStatusService;

    /**
     * 接收ZBX事件，处理
     *
     * @param problemMessage ZBX事件
     */
    @Override
    public void receiveProblems(ProblemMessage problemMessage) {
        ZbxProblem zbxProblem = problemMessage.getData();

        List<Tag> itemTags = zbxProblem.getItemTags();
        Map<String, String> tagMap = itemTags.stream().collect(Collectors.toMap(Tag::getTag, Tag::getValue));

        if (tagMap.containsKey(IotConstants.DEVICE_STATUS_OFFLINE_TAG)) {
            // 设备离线
            this.deviceOffline(problemMessage);
        } else if (tagMap.containsKey(IotConstants.DEVICE_STATUS_ONLINE_TAG)) {
            // 设备上线
            log.debug("设备上线：{}", zbxProblem);
            deviceStatusService.online(zbxProblem.getHostname(), zbxProblem.getClock());
            problemMessage.ack();
        } else if (tagMap.containsKey(IotConstants.ALARM_TAG_NAME)) {
            // 设备告警
            // hostname = device_id = 1623590722269122560
            // name= 事件id event_rule_id
            alarmRecord(zbxProblem);
            // zbxProblem(eventid=32956, hostname=1623590722269122560, severity=3, name=1623589728705613824, clock=1676269468, groups=[1611599188680720384, 1611785596489895936], itemTags=[ZbxTag(tag=__alarm__, value=1623590722269122560), ZbxTag(tag=__product_id__, value=1623587503962886144), ZbxTag(tag=__attribute_key__, value=temp)])
            log.debug("设备告警：{}", zbxProblem);
            ProductEvent productEvent = productEventService.getById(zbxProblem.getName());
            if (productEvent == null) {
                log.warn("告警事件不存在：{}", zbxProblem);
                problemMessage.ack();
                return;
            }
            // 发送到EMQX
            MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(MqttMessageType.ALARM, zbxProblem.getHostname(), zbxProblem.getSeverity(), productEvent.getName());
            emqxClient.publish("iot/device/" + zbxProblem.getHostname() + "/problem/" + zbxProblem.getSeverity(), JsonUtils.toJsonString(mqttMessageDTO));
            if (tagMap.containsKey(IotConstants.EXECUTE_TAG_NAME)) {
                deviceServiceExecute(zbxProblem);
            }

            problemMessage.ack();
        } else {
            log.warn("未知事件：{}", zbxProblem);
            problemMessage.ack();
        }
    }


    /**
     * 设备离线
     *
     * @param problemMessage 问题信息
     */
    private void deviceOffline(ProblemMessage problemMessage) {
        ZbxProblem zbxProblem = problemMessage.getData();
        // 如果超过3秒则不执行设备离线函数
        LocalDateTime problemTime = TimeUtil.toLocalDateTime(zbxProblem.getClock());
        if (TimeUtil.getSecondBetween(problemTime, LocalDateTime.now()) > 3) {
            log.debug("设备离线超时：{}", zbxProblem);
            problemMessage.ack();
            return;
        }
        // 设备离线
        deviceStatusService.offline(zbxProblem.getHostname());
        log.debug("设备离线：{}", zbxProblem);
        problemMessage.ack();
    }

    /**
     * 设备功能执行
     */
    private void deviceServiceExecute(ZbxProblem zbxProblem) {
        String eventRuleId = zbxProblem.getName();
        String deviceId = zbxProblem.getHostname();
        // 执行功能
        deviceService.executeService(NumberUtil.parseLong(deviceId), NumberUtil.parseLong(eventRuleId), 1);
        // 设备功能
        log.info("设备服务：{}", zbxProblem);
    }


    /**
     * 报警记录
     *
     * @param zbxProblem zbx问题
     */
    private void alarmRecord(ZbxProblem zbxProblem) {
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setObjectId(Long.valueOf(zbxProblem.getName()));
        alarmDTO.setSeverity(zbxProblem.getSeverity());
        alarmDTO.setDeviceId(Long.valueOf(zbxProblem.getHostname()));
        alarmDTO.setEventId(zbxProblem.getEventid().longValue());
        alarmDTO.setAcknowledged(0L);
        alarmDTO.setClock(TimeUtil.toLocalDateTime(zbxProblem.getClock()));
        problemService.addAlarmRecord(alarmDTO);
    }


}
