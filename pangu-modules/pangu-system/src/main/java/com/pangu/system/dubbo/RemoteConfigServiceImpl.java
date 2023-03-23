package com.pangu.system.dubbo;

import com.pangu.system.api.RemoteConfigService;
import com.pangu.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 操作Config
 *
 * @author chengliang4810
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteConfigServiceImpl implements RemoteConfigService {

    private final ISysConfigService configService;

    @Override
    public String getConfigByKey(String configKey) {
        log.info("getConfigByKey: {}", configKey);
        return configService.selectConfigByKey(configKey);
    }

    @Override
    public Boolean editValue(String key, String value) {
        try {
            configService.updateConfigValueByKey(key, value);
            return true;
        } catch (Exception e) {
            log.error("editValue error: {}", e.getMessage());
            return false;
        }
    }

}
