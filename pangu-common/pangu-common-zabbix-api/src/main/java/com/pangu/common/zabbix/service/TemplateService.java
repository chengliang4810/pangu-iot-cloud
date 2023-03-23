package com.pangu.common.zabbix.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxTemplate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 模板服务
 *
 * @author chengliang
 * @date 2022/11/01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateService {

    private final ZbxTemplate zbxTemplate;

    /**
     * 创建 zabbix 模板
     * @param hostGroupId hostGroupId host分组ID
     * @param templateName zabbix 模板名称
     * @return {@link String} zbxId
     */
    public String zbxTemplateCreate(String hostGroupId, String templateName) {
        // 全局分组
        log.debug("create template {} , default group id {}", templateName, hostGroupId);
        String result = zbxTemplate.templateCreate(templateName, hostGroupId);
        return JsonUtils.parseObject(result, TemplateIds.class).getTemplateids()[0];
    }

    /**
     * 根据模板ID 删除模板
     *
     * @param templateId 模板ID
     * @return
     */
    public void zbxTemplateDelete(String templateId) {
        JSONArray jsonArray = JSONUtil.parseArray(zbxTemplate.templateGet(templateId));
        if (jsonArray.size() > 0) {
            zbxTemplate.templateDelete(templateId);
        } else {
            log.warn("templateId {} not found", templateId);
        }
    }

    @Data
    static class TemplateIds {
        private String[] templateids;
        private String[] valuemapids;
    }

}
