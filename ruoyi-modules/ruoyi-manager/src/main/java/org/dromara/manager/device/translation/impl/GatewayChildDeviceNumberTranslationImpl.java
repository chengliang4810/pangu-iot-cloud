package org.dromara.manager.device.translation.impl;

import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.common.translation.annotation.TranslationType;
import org.dromara.common.translation.core.TranslationInterface;
import org.dromara.manager.device.service.IGatewayBindRelationService;
import org.dromara.manager.device.translation.DeviceTransConstant;
import org.springframework.stereotype.Component;

/**
 * 网关子设备数量翻译实现
 *
 * @author chengliang4810
 */
@Component
@RequiredArgsConstructor
@TranslationType(type = DeviceTransConstant.GATEWAY_ID_TO_CHILD_DEVICE_NUMBER)
public class GatewayChildDeviceNumberTranslationImpl implements TranslationInterface<Long> {

    private final IGatewayBindRelationService gatewayBindRelationService;

    @Override
    public Long translation(Object deviceId, String deviceType) {
        // 只有网关设备才有子设备数量
        if (deviceId == null) {
            return 0L;
        }
        return gatewayBindRelationService.countChildDevice(NumberUtil.parseLong(deviceId.toString()));
    }

}
