package com.pangu.iot.data.zabbix;

import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

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
        zbxValue.getItemTags().parallelStream().filter(itemTag -> itemTag.getTag().equals("productId")).findFirst().ifPresent(itemTag -> {
            Map<String, Object> value = new LinkedHashMap<>(3);
            value.put("ts", Timestamp.valueOf(LocalDateTime.ofEpochSecond(zbxValue.getClock(), zbxValue.getNs(), ZoneOffset.of("+8"))   ));
            value.put("online", 1);
            value.put(zbxValue.getName(), zbxValue.getValue());
            tdEngineService.insertData(zbxValue.getHostname(),SUPER_TABLE_PREFIX + itemTag.getValue(), value);
        });
    }


    /**
     * 接收ZBX事件，处理
     *
     * @param zbxProblem ZBX事件
     */
    @Override
    public void receiveProblems(ZbxProblem zbxProblem) {
        // 设备告警
        log.info("接收到ZBX事件：{}", zbxProblem);
    }

}
