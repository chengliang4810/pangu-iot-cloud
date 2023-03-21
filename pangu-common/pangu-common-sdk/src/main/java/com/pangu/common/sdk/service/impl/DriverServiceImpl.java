package com.pangu.common.sdk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.domain.dto.DeviceExecuteResult;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.utils.EmqxUtil;
import com.pangu.common.sdk.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class DriverServiceImpl  implements DriverService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void notifyDeviceFunctionResult(DeviceExecuteResult deviceExecuteResult) {
        EmqxUtil.getClient().publish("iot/device/" + deviceExecuteResult.getDeviceId() + "/function/" + deviceExecuteResult.getIdentifier() + "/exec/callback", JsonUtils.toJsonString(deviceExecuteResult));
    }

    /**
     * 驱动程序元数据同步
     *
     * @param primaryKey 主键
     */
    @Override
    public void driverMetadataSync(String primaryKey) {
        // mqttClient.publish();
    }


    /**
     * Close ApplicationContext
     *
     * @param template Template
     * @param params   Object Params
     */
    @Override
    public void close(CharSequence template, Object... params) {
        log.error(StrUtil.format(template, params));
        ((ConfigurableApplicationContext) applicationContext).close();
        System.exit(1);
    }


}
