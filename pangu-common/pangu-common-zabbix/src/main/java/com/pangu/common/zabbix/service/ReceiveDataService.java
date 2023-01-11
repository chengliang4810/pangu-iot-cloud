package com.pangu.common.zabbix.service;

import com.pangu.common.zabbix.model.ZbxProblem;
import com.pangu.common.zabbix.model.ZbxValue;

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
     * @param itemValue ZBX数据
     */
    void receiveData(ZbxValue itemValue);

    /**
     * 接收ZBX事件，处理
     *
     * @param itemValue ZBX事件
     */
    void receiveProblems(ZbxProblem itemValue);


}
