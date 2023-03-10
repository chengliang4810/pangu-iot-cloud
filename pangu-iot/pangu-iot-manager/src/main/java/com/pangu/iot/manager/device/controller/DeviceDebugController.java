package com.pangu.iot.manager.device.controller;

import com.pangu.common.core.domain.R;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.common.zabbix.model.ZbxResponse;
import com.pangu.common.zabbix.service.SenderDataService;
import com.pangu.manager.api.domain.Device;
import com.pangu.iot.manager.device.domain.bo.DebugDataBO;
import com.pangu.iot.manager.device.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("/device/debug")
public class DeviceDebugController {

    private final SenderDataService senderDataService;

    private final IDeviceService deviceService;

    /**
     * 设备调试发送数据
     */
    @PostMapping("/sendData")
    public R<ZbxResponse> sendData(@RequestBody DebugDataBO data) {
        Device device = deviceService.getById(data.getDeviceId());
        Assert.notNull(device, "设备不存在");
        Map<String, String> attribute = data.getParams().stream().collect(Collectors.toMap(DebugDataBO.Params::getDeviceAttrKey, DebugDataBO.Params::getDeviceAttrValue));
        // 封装设备数据
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.setDeviceId(String.valueOf(device.getId()));
        deviceValue.setAttributes(attribute);
        deviceValue.setClock(System.currentTimeMillis() / 1000);
        ZbxResponse response = senderDataService.sendData(deviceValue);
        if (response.getFailed() <= 0){
            return R.ok("数据发送成功");
        }else {
            return R.fail("数据上报失败");
        }
    }
}
