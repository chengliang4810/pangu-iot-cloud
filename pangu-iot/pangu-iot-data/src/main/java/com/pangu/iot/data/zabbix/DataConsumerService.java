package com.pangu.iot.data.zabbix;

import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxTag;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import com.pangu.iot.data.service.DeviceStatusService;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
        // 存入tdengine
        zbxValue.getItemTags().parallelStream().filter(itemTag -> itemTag.getTag().equals(IotConstants.PRODUCT_ID_TAG_NAME)).findFirst().ifPresent(itemTag -> {
            // 获取当前设备状态
            int status = deviceStatusService.getStatus(itemTag.getValue(), zbxValue.getHostname());
            Map<String, Object> value = new LinkedHashMap<>(3);
            value.put("ts", Timestamp.valueOf(LocalDateTime.ofEpochSecond(zbxValue.getClock(), zbxValue.getNs(), ZoneOffset.of("+8"))   ));
            value.put("online", status);
            value.put(zbxValue.getName(), zbxValue.getValue());
            tdEngineService.insertData( StrUtil.format(IotConstants.DEVICE_TABLE_NAME_TEMPLATE, itemTag.getValue(), zbxValue.getHostname() ),SUPER_TABLE_PREFIX + itemTag.getValue(), value);
        });
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
            deviceStatusService.changeStatus(tagMap.get(IotConstants.PRODUCT_ID_TAG_NAME), tagMap.get(IotConstants.DEVICE_STATUS_OFFLINE_TAG), 0);
            log.info("设备离线：{}", zbxProblem);
        } else if (tagMap.containsKey(IotConstants.DEVICE_STATUS_ONLINE_TAG)) {
            // 设备上线
            log.info("设备上线：{}", zbxProblem);
            deviceStatusService.changeStatus(tagMap.get(IotConstants.PRODUCT_ID_TAG_NAME), tagMap.get(IotConstants.DEVICE_STATUS_ONLINE_TAG), 1);
        }
    }

}
