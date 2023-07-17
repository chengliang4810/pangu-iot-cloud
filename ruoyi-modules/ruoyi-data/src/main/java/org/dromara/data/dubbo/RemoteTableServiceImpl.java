package org.dromara.data.dubbo;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.data.api.RemoteTableService;
import org.dromara.data.service.TdEngineService;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
        tdEngineService.initSuperTable(productId.toString());
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
            tdEngineService.dropSuperTable(productId.toString());
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
            tdEngineService.createSuperTableField(productId.toString(), identifier, attributeType);
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
            tdEngineService.dropSuperTableField(productId.toString(), identifier);
        } catch (Exception e) {
            throw new ServiceException("删除超级表失败: {}", e.getMessage());
        }
    }

    /**
     * 创建表
     *
     * @param productId 产品id
     * @param deviceCode  设备编号
     */
    @Override
    public void createTable(Long productId, String deviceCode) {
        Assert.notNull(productId, "产品id不能为空");
        Assert.notBlank(deviceCode, "设备编号不能为空");
        try {
            tdEngineService.createTable(productId.toString(), deviceCode);
        } catch (Exception e) {
            throw new ServiceException("创建表失败: {}", e.getMessage());
        }
    }

    /**
     * 删除表
     *
     * @param id id
     */
    @Override
    public void dropTable(Long id) {
        Assert.notNull(id, "id不能为空");
        try {
            tdEngineService.dropTable(Collections.singletonList(id.toString()));
        } catch (Exception e) {
            throw new ServiceException("删除表失败: {}", e.getMessage());
        }
    }
}