package com.pangu.common.zabbix.service;

import cn.hutool.core.util.StrUtil;
import com.dtflys.forest.config.ForestConfiguration;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxHostGroup;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 主机分组服务
 *
 * @author chengliang
 * @date 2022/11/01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HostGroupService {

    private final ZbxHostGroup zbxHostGroup;

    private final ForestConfiguration configuration;

    private final HostGroupCallback hostGroupCallback;

    private static String zbxApiToken;

    /**
     * 初始化Zabbix主机组
     */
    @PostConstruct
    private void initHostGroup() {
        log.debug("初始化Zabbix主机组 {}", configuration);
        zbxApiToken = configuration.getVariableValue("zbxApiToken").toString();
        // 获取全局主机分组
        String groupId = getGlobalHostGroup();
        if (StrUtil.isBlank(groupId)){
            // 创建全局主机组
            groupId = createGlobalHostGroup();
        }

        hostGroupCallback.initAfter(groupId);
    }

    /**
     * 创建默认全局主机组
     *
     * @return Integer
     */
    public String createGlobalHostGroup() {
        String response = zbxHostGroup.createGlobalHostGroup(zbxApiToken);
        return JsonUtils.parseObject(response, ZbxResponseIds.class).getGroupids()[0];
    }

    /**
     * 查询全局主机组
     *
     * @return String
     */
    private String getGlobalHostGroup() {
        String response = zbxHostGroup.getGlobalHostGroup(zbxApiToken);
        List<Map<String, String>> ids = JsonUtils.parseObject(response, List.class);
        if (null != ids && ids.size() > 0) {
            return ids.get(0).get("groupid");
        }
        return "";
    }


    /**
     * 创建主机组
     *
     * @param hostGroupName 主机组名称
     * @return zbxId {@link String}
     */
    public String createHostGroup(String hostGroupName){
        String response = zbxHostGroup.hostGroupCreate(hostGroupName);
        return JsonUtils.parseObject(response, ZbxResponseIds.class).getGroupids()[0];
    }

    /**
     * 主机组删除
     *
     * @param zbxIds zbx id
     */
    public void hostGroupDelete(List<String> zbxIds) {
        String result = zbxHostGroup.hostGroupDelete(zbxIds);
        log.debug("主机组删除结果：{}", result);
    }


    @Data
    static class ZbxResponseIds {
        String[] groupids;
        String[] userids;
        String[] usrgrpids;
    }

}
