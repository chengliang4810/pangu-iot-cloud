package com.pangu.common.zabbix.service;

import com.pangu.common.zabbix.domain.DataMessage;

/**
 * 接收ZBX数据服务
 *
 * @author chengliang4810
 * @date 2023/01/11 11:15
 */
public interface ReceiveDataService {


    /**
     * 接收ZBX数据，处理
     *
     * @param dataMessage ZBX数据
     */
    void receiveData(DataMessage dataMessage);

}
