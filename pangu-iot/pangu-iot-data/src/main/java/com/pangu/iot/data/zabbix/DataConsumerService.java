package com.pangu.iot.data.zabbix;

import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        // 过滤Zabbix server，暂不处理
        if (zbxValue.getHostname().equals("Zabbix server")){
            return;
        }
        // 存入tdengine
        log.info("接收到ZBX数据：{}", zbxValue);
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
