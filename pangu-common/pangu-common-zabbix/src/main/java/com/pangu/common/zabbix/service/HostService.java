package com.pangu.common.zabbix.service;

import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxHost;
import com.pangu.common.zabbix.entity.Interface;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    @Data
    static class HostIds {
        private String[] hostids;
    }

}
