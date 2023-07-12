package org.dromara.manager.product.service.impl;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.data.api.RemoteTableService;
import org.dromara.manager.device.service.IDeviceAttributeService;
import org.dromara.manager.device.service.IDeviceService;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.service.IProductBatchService;
import org.dromara.manager.product.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchService implements IProductBatchService {

    @DubboReference
    private RemoteTableService remoteTableService;

    private final IDeviceService deviceService;
    private final IProductService productService;
    private final IDeviceAttributeService deviceAttributeService;

    @Override
    public List<ProductVo> queryParentDeviceProduct(Long deviceId) {
        return productService.queryParentDeviceProduct(deviceId);
    }

    /**
     * 批量删除产品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
            ids.forEach(id -> {
                Long deviceNumber = deviceService.countDeviceByProductId(id);
                Assert.isFalse(deviceNumber > 0, "产品已被使用,请先删除设备");
            });
        }

        // 记录删除数量
        AtomicInteger count = new AtomicInteger(0);

        ids.forEach(id -> {
            // 删除产品功能
            // 删除产品标签
            // 删除产品属性
            deviceAttributeService.deleteByProductId(id);
            // 删除产品
            Boolean result = productService.deleteById(id);
            if (result) {
                count.getAndIncrement();
            }
            remoteTableService.dropSuperTable(id);
        });


        return count.get() > 0;
    }



}
