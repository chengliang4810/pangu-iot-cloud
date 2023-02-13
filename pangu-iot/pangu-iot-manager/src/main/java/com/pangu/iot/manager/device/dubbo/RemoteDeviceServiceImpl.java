package com.pangu.iot.manager.device.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.manager.api.RemoteDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * 操作TdEngine服务
 *
 * @author chengliang4810
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteDeviceServiceImpl implements RemoteDeviceService {

    private final IDeviceService deviceService;

    /**
     * 更新设备最后上线时间
     *
     * @param deviceCode 设备代码
     * @param clock       时间
     */
    @Override
    public void updateDeviceLastOnlineTime(String deviceCode, int clock) {
        LocalDateTime localDateTime = TimeUtil.toLocalDateTime(clock);
        Date time = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        deviceService.update(new Device().setLatestOnline(time), Wrappers.lambdaQuery(Device.class).eq(Device::getCode, deviceCode));
    }

}
