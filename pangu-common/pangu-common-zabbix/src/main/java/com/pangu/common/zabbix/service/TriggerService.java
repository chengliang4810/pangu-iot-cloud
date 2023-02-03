package com.pangu.common.zabbix.service;

import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxTrigger;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 触发服务
 *
 * @author chengliang4810
 * @date 2023/02/03 11:17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TriggerService {

    private final ZbxTrigger zbxTrigger;

    /**
     * 创建zbx触发器
     *
     * @param triggerName 触发器名字
     * @param expression  表达式
     * @param level       告警级别
     * @return {@link String[]}
     */
    public String createZbxTrigger(String triggerName, String expression, Integer level, Map<String, String> tags) {
        String res = zbxTrigger.triggerCreate(triggerName, expression, level,0, tags);
        return JsonUtils.parseObject(res, TriggerIds.class).getTriggerids()[0];
    }

    /**
     * 触发标签创建
     *
     * @param triggerId 触发id
     * @param tags      标签
     */
    public void triggerTagCreate(String triggerId, Map<String, String> tags) {
        zbxTrigger.triggerTagCreate(triggerId, tags);
    }


    @Data
    static class TriggerIds {
        private String[] triggerids;
    }

}
