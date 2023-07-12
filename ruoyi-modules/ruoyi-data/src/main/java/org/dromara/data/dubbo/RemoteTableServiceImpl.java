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

    /**
     * 添加超级表字段
     *
     * @param productId     产品id
     * @param identifier    标识符
     * @param attributeType 属性类型
     */
    @Override
    public void addSuperTableField(Long productId, String identifier, String attributeType) {
        Assert.notNull(productId, "产品id不能为空");
        Assert.notBlank(identifier, "标识符不能为空");
        Assert.notBlank(attributeType, "属性类型不能为空");
        try {
            tdEngineService.createSuperTableField(String.valueOf(productId), identifier, attributeType);
        } catch (Exception e) {
            throw new ServiceException("删除超级表失败: {}", e.getMessage());
        }
    }

    /**
     * 删除超级表字段
     *
     * @param productId  产品id
     * @param identifier 标识符
     */
    @Override
    public void deleteSuperTableField(Long productId, String identifier) {
        Assert.notNull(productId, "产品id不能为空");
        Assert.notBlank(identifier, "标识符不能为空");
        try {
            tdEngineService.dropSuperTableField(String.valueOf(productId), identifier);
        } catch (Exception e) {
            throw new ServiceException("删除超级表失败: {}", e.getMessage());
        }
    }
}
