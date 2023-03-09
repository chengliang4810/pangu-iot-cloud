package com.pangu.iot.manager.device.controller;

import com.pangu.common.core.domain.R;
import com.pangu.common.zabbix.model.ZbxResponse;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteBO;
import com.pangu.iot.manager.device.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备调试控制器
 *
 * @author chengliang
 * @date 2023/01/08
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/device/service")
public class DeviceServiceController {

    private final IDeviceService deviceService;

    /**
     * 设备调试发送数据
     */
    @PostMapping("/execute")
    public R<ZbxResponse> execute(@RequestBody ServiceExecuteBO serviceExecute) {
        deviceService.executeService(serviceExecute);
        return R.ok();
    }

}
