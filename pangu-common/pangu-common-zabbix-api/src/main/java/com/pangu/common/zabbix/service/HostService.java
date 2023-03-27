package com.pangu.common.zabbix.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxHost;
import com.pangu.common.zabbix.entity.Interface;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HostService {

    private final ZbxHost zbxHost;

    private final static Interface defaultInterface = new Interface().setIp("127.0.0.1").setPort("10050").setUseip("1").setType("1").setMain("1");

    /**
     * 主机创建
     *
     * @param host         主机名
     * @param hostGroupIds 主机组id
     * @param templateId   模板id
     * @return {@link String}
     */
    public String hostCreate(String host, List<String> hostGroupIds, String templateId, String proxyId) {
        String result = zbxHost.hostCreate(host, hostGroupIds, templateId, proxyId, defaultInterface);
        return JsonUtils.parseObject(result, HostIds.class).getHostids()[0];
    }

    /**
     * 主机查询
     * @param zbxIds
     */
    public void hostGet(List<String> zbxIds) {
    }

    /**
     * 主机删除
     * 默认checkExist=true
     * @param zbxIds zbx id
     */
    public void hostDelete(List<String> zbxIds) {
        this.hostDelete(zbxIds, true);
    }

    /**
     * 主机删除
     *
     * @param zbxIds     zbx id
     * @param checkExist 检查存在
     */
    public void hostDelete(List<String> zbxIds, boolean checkExist) {

        // 不需要检查是否存在，直接调用删除
        if (!checkExist){
            zbxHost.hostDelete(zbxIds);
            return;
        }
        zbxIds.forEach(this::hostDelete);
    }

    /**
     * 主机删除
     * 默认checkExist=true
     * @param zbxId zbx id
     */
    private void hostDelete(String zbxId) {
        this.hostDelete(zbxId, true);
    }

    /**
     * 主机删除
     *
     * @param zbxId      zbx id
     * @param checkExist 检查存在
     */
    public void hostDelete(String zbxId, boolean checkExist) {
        if (StrUtil.isBlank(zbxId)) {
            return;
        }
        if (!checkExist){
            zbxHost.hostDelete(Collections.singletonList(zbxId));
        }
        JSONArray jsonArray = JSONUtil.parseArray(zbxHost.hostDetail(zbxId));
        if (jsonArray.size() > 0) {
            zbxHost.hostDelete(Collections.singletonList(zbxId));
        }
    }

    @Data
    static class HostIds {
        private String[] hostids;
    }

}
