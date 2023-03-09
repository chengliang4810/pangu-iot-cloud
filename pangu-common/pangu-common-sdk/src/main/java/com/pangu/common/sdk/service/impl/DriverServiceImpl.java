package com.pangu.common.sdk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.pangu.common.emqx.doamin.EmqxClient;
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
    @Resource
    private EmqxClient emqxClient;

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
