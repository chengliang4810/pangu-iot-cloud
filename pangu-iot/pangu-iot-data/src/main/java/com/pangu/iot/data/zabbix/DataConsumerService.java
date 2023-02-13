package com.pangu.iot.data.zabbix;

import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxTag;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import com.pangu.manager.api.RemoteAlarmService;
import com.pangu.manager.api.model.AlarmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.SUPER_TABLE_PREFIX;

/**
 * ZBX数据消费者服务
 *
 * @author chengliang4810
 * @date 2023/01/11 12:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataConsumerService implements ReceiveDataService {

    private final TdEngineService tdEngineService;
    @DubboReference
    private final RemoteAlarmService remoteAlarmService;
    private final DeviceStatusService deviceStatusService;

    /**
     * 接收ZBX数据，处理
     *
     * @param zbxValue ZBX数据
     */
    @Override
    public void receiveData(ZbxValue zbxValue) {
        // 过滤Zabbix server，暂不处理
        if (zbxValue.getHostname().equals("Zabbix server")){
            return;
        }
        log.info("接收到ZBX数据：{}", zbxValue);
        Map<String, String> tagMap = zbxValue.getItemTags().stream().collect(Collectors.toMap(ZbxTag::getTag, ZbxTag::getValue));
        if (!tagMap.containsKey(IotConstants.PRODUCT_ID_TAG_NAME)){
            log.info("未知产品：{}", zbxValue);
            return;
        }
        // 存入tdengine
        // 产品Id
        String productId = tagMap.get(IotConstants.PRODUCT_ID_TAG_NAME);
        // 获取当前设备状态
        boolean status = deviceStatusService.getOnlineStatus(zbxValue.getHostname());

        Map<String, Object> value = new LinkedHashMap<>(3);
        value.put(IotConstants.TABLE_STATUS_FIELD, status ? 1 : 0);
        value.put(tagMap.get(IotConstants.ATTRIBUTE_KEY_TAG_NAME), zbxValue.getValue());
        value.put(IotConstants.TABLE_PRIMARY_FIELD, TimeUtil.toTimestamp(zbxValue.getClock(), zbxValue.getNs()));
        tdEngineService.insertData(IotConstants.DEVICE_TABLE_NAME_PREFIX + zbxValue.getHostname(),SUPER_TABLE_PREFIX + productId, value);
    }


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
            // log.info("设备离线：{}", zbxProblem);
        } else if (tagMap.containsKey(IotConstants.DEVICE_STATUS_ONLINE_TAG)) {
            // 设备上线
            log.info("设备上线：{}", zbxProblem);
            deviceStatusService.online(zbxProblem.getHostname(), zbxProblem.getClock());
        } else if (tagMap.containsKey(IotConstants.ALARM_TAG_NAME)){
            // 设备告警
            // hostname = device_id = 1623590722269122560
            // name= 事件id event_rule_id
            AlarmDTO alarmDTO = new AlarmDTO();
            alarmDTO.setObjectId(Long.valueOf(zbxProblem.getName()));
            alarmDTO.setSeverity(zbxProblem.getSeverity());
            alarmDTO.setDeviceId(Long.valueOf(zbxProblem.getHostname()));
            alarmDTO.setEventId(zbxProblem.getEventid().longValue());
            alarmDTO.setAcknowledged(0L);
            alarmDTO.setClock(TimeUtil.toLocalDateTime(zbxProblem.getClock()));
            remoteAlarmService.addAlarmRecord(alarmDTO);
            // zbxProblem(eventid=32956, hostname=1623590722269122560, severity=3, name=1623589728705613824, clock=1676269468, groups=[1611599188680720384, 1611785596489895936], itemTags=[ZbxTag(tag=__alarm__, value=1623590722269122560), ZbxTag(tag=__product_id__, value=1623587503962886144), ZbxTag(tag=__attribute_key__, value=temp)])
            // ZbxProblem(eventid=32957, hostname=1623590722269122560, severity=3, name=1623589984432328704, clock=1676269468, groups=[1611599188680720384, 1611785596489895936], itemTags=[ZbxTag(tag=__alarm__, value=1623590722269122560), ZbxTag(tag=__product_id__, value=1623587503962886144), ZbxTag(tag=__attribute_key__, value=temp)])
            log.info("设备告警：{}", zbxProblem);
        } else if (tagMap.containsKey(IotConstants.EXECUTE_TAG_NAME)){
            // 设备服务
            log.info("设备服务：{}", zbxProblem);
        } else {
            log.info("未知事件：{}", zbxProblem);
        }
    }

}
