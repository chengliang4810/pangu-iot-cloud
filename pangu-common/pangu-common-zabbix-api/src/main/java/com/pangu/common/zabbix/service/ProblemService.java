package com.pangu.common.zabbix.service;

import com.pangu.common.zabbix.api.ZbxProblem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 问题服务
 *
 * @author chengliang4810
 * @date 2023/02/13 16:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ZbxProblem zbxProblem;

    /**
     * 确认问题
     */
    public void acknowledgeProblem(String problemId) {
        zbxProblem.acknowledgement(problemId, 2);
    }

    /**
     * 关闭问题
     *
     * @param problemId 问题id
     */
    public void resolve(String problemId) {
        zbxProblem.acknowledgement(problemId, 1);
    }

}
