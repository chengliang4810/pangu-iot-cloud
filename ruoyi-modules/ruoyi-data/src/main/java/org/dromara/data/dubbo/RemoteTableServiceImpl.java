package org.dromara.data.dubbo;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.data.api.RemoteTableService;
import org.dromara.data.service.TdEngineService;
import org.springframework.stereotype.Service;

/**
 * 远程表服务实现
 *
 * @author chengliang
 * @date 2023/07/12
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteTableServiceImpl implements RemoteTableService {

    private final TdEngineService tdEngineService;

    /**
     * 初始化超级表
     *
     * @param productId 产品id
     */
    @Override
    public void initSuperTable(Long productId) {
        Assert.notNull(productId, "产品id不能为空");
        tdEngineService.initSuperTable(String.valueOf(productId));
    }

    /**
     * 删除超级表
     *
     * @param productId 产品id
     */
    @Override
    public void dropSuperTable(Long productId) {
        Assert.notNull(productId, "产品id不能为空");
        try {
            tdEngineService.dropSuperTable(String.valueOf(productId));
        } catch (Exception e) {
            throw new ServiceException("删除超级表失败: {}", e.getMessage());
        }
    }
}
