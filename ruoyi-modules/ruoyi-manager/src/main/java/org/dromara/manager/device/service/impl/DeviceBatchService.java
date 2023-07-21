package org.dromara.manager.device.service.impl;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.data.api.RemoteTableService;
import org.dromara.manager.device.domain.vo.DeviceVo;
import org.dromara.manager.thing.service.IThingAttributeService;
import org.dromara.manager.device.service.IDeviceBatchService;
import org.dromara.manager.device.service.IDeviceService;
import org.dromara.manager.device.service.IGatewayBindRelationService;
import org.dromara.manager.driver.service.IDriverAttributeValueService;
import org.dromara.manager.driver.service.IPointAttributeValueService;
import org.dromara.manager.product.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceBatchService implements IDeviceBatchService {

    @DubboReference
    private RemoteTableService remoteTableService;
    private final IDeviceService deviceService;
    private final IProductService productService;
    private final IThingAttributeService deviceAttributeService;
    private final IGatewayBindRelationService gatewayBindRelationService;
    private final IPointAttributeValueService pointAttributeValueService;
    private final IDriverAttributeValueService driverAttributeValueService;

    /**
     * 批量删除设备
     *
     * @param ids
     * @param isValid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }

        // 记录删除数量
        AtomicInteger count = new AtomicInteger();

        ids.forEach(id -> {
            DeviceVo deviceVo = deviceService.queryById(id);
            if (deviceVo == null) {
                // 设备不存在,跳过
                return;
            }

            // 检查是否关联子设备
            Long deviceNumber = deviceService.countChildByDeviceId(deviceVo.getId(), null);
            Assert.isFalse(deviceNumber > 0, "设备[{}]存在子设备,请先删除子设备", deviceVo.getName());

            // 删除网关绑定关系
            gatewayBindRelationService.deleteByDeviceId(deviceVo.getId());

            // 删除物模型属性
            deviceAttributeService.deleteByDeviceId(deviceVo.getId());

            // 删除驱动属性配置
            driverAttributeValueService.deleteByDeviceId(deviceVo.getId());

            // 删除物模型属性配置
            pointAttributeValueService.deleteByDeviceId(deviceVo.getId());

            // 更新产品设备数量
            productService.updateDeviceNumber(deviceVo.getProductId(), -1);
            //
            remoteTableService.dropTable(deviceVo.getId());
            // 删除设备
            boolean result = deviceService.deleteById(deviceVo.getId());
            count.addAndGet(result ? 1:0);

        });

        return count.get() > 0;
    }

}
