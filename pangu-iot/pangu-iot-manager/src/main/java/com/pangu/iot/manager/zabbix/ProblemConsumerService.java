package com.pangu.iot.manager.zabbix;

import cn.hutool.core.util.NumberUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxTag;
import com.pangu.common.zabbix.service.ReceiveProblemService;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.data.api.RemoteDeviceStatusService;
import com.pangu.iot.manager.alarm.service.IProblemService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.manager.api.model.AlarmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemConsumerService implements ReceiveProblemService {

    private final IDeviceService deviceService;
    private final IProblemService problemService;
    private final RemoteDeviceStatusService deviceStatusService;

    /**
     * 接收ZBX事件，处理
     *
     * @param zbxProblem ZBX事件
     */
    @Override
    public void receiveProblems(ZbxProblem zbxProblem) {
        // 设备告警等
        // log.info("接收到ZBX事件：{}", zbxProblem);

        List<ZbxTag> itemTags = zbxProblem.getItemTags();
        Map<String, String> tagMap = itemTags.stream().collect(Collectors.toMap(ZbxTag::getTag, ZbxTag::getValue));

        if (tagMap.containsKey(IotConstants.DEVICE_STATUS_OFFLINE_TAG)){
            // 设备离线
            deviceStatusService.offline(zbxProblem.getHostname());
            log.info("设备离线：{}", zbxProblem);
        } else if (tagMap.containsKey(IotConstants.DEVICE_STATUS_ONLINE_TAG)) {
            // 设备上线
            log.info("设备上线：{}", zbxProblem);
            deviceStatusService.online(zbxProblem.getHostname(), zbxProblem.getClock());
        } else if (tagMap.containsKey(IotConstants.ALARM_TAG_NAME)){
            // 设备告警
            // hostname = device_id = 1623590722269122560
            // name= 事件id event_rule_id
            alarmRecord(zbxProblem);
            // zbxProblem(eventid=32956, hostname=1623590722269122560, severity=3, name=1623589728705613824, clock=1676269468, groups=[1611599188680720384, 1611785596489895936], itemTags=[ZbxTag(tag=__alarm__, value=1623590722269122560), ZbxTag(tag=__product_id__, value=1623587503962886144), ZbxTag(tag=__attribute_key__, value=temp)])
            log.info("设备告警：{}", zbxProblem);

            if (tagMap.containsKey(IotConstants.EXECUTE_TAG_NAME)){
                deviceServiceExecute(zbxProblem);
            }

        } else {
            log.info("未知事件：{}", zbxProblem);
        }
    }

    /**
     * 设备功能执行
     */
    private void deviceServiceExecute(ZbxProblem zbxProblem){
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
    private void alarmRecord(ZbxProblem zbxProblem){
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
