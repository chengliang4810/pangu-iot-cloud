package com.pangu.iot.manager.product.service.zabbix;

import com.pangu.common.zabbix.service.HostGroupCallback;
import com.pangu.system.api.RemoteConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.pangu.common.core.constant.ConfigKeyConstants.GLOBAL_HOST_GROUP_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class HostGroupImpl implements HostGroupCallback {

    private final RemoteConfigService configService;

    @Override
    public void initAfter(String groupId) {
        // 同步到系统SysConfig中
//        SysConfig sysConfig = new SysConfig();
//        sysConfig.setConfigKey(GLOBAL_HOST_GROUP_KEY);
//        sysConfig.setConfigValue(groupId);
        configService.editValue(GLOBAL_HOST_GROUP_KEY, groupId);
    }

}
