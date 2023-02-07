package com.pangu.iot.data.dubbo;

import com.pangu.iot.data.service.DeviceStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteDeviceStatusServiceImpl implements com.pangu.data.api.RemoteDeviceStatusService {

    private final DeviceStatusService deviceStatusService;

    /**
     * 通过设备Code获取设备在线状态以及时间
     *
     * @param deviceCode 设备代码
     * @return {@link Map}<{@link String}, {@link Integer}>
     */
    @Override
    public Map<String, Integer> getDeviceOnlineStatus(Set<String> deviceCode) {
        return deviceStatusService.getOnlineStatus(deviceCode);
    }


}
