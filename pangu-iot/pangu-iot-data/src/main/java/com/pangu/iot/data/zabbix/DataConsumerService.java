package com.pangu.iot.data.zabbix;

import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * ZBX数据消费者服务
 *
 * @author chengliang4810
 * @date 2023/01/11 12:01
 */
@Slf4j
@Service
public class DataConsumerService implements ReceiveDataService {


    /**
     * 接收ZBX数据，处理
     *
     * @param zbxValue ZBX数据
     */
    @Override
    public void receiveData(ZbxValue zbxValue) {
        if (zbxValue.getHostname().equals("Zabbix server")){
            return;
        }
        long second = LocalDateTime.ofEpochSecond(zbxValue.getClock(), zbxValue.getNs(), ZoneOffset.of("+8")).toEpochSecond(ZoneOffset.of("+8"));
        System.out.println(JsonUtils.toJsonString(zbxValue));
    }


    /**
     * 接收ZBX事件，处理
     *
     * @param zbxProblem ZBX事件
     */
    @Override
    public void receiveProblems(ZbxProblem zbxProblem) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(zbxProblem.getClock())));
        System.out.println(JsonUtils.toJsonString(zbxProblem));
    }

}
