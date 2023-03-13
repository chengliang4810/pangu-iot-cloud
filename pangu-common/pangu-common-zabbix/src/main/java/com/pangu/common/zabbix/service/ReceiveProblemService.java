package com.pangu.common.zabbix.service;

import com.pangu.common.zabbix.domain.ProblemMessage;

/**
 * 接收ZBX问题服务
 *
 * @author chengliang4810
 * @date 2023/01/11 11:15
 */
public interface ReceiveProblemService {

    /**
     * 接收ZBX事件，处理
     *
     * @param problemMessage ZBX事件
     */
    void receiveProblems(ProblemMessage problemMessage);


}
