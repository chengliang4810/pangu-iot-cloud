package org.dromara.data.dubbo;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.data.api.RemoteTableService;
import org.dromara.data.service.TdEngineService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

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

    /**
     * 重命名表
     * 删除旧表, 创建新表
     *
     * @param productId     产品id
     * @param oldDeviceCode 旧设备代码
     * @param newDeviceCode 新设备代码
     */
    @Override
    public void renameTable(Long productId, String oldDeviceCode, String newDeviceCode) {
        Assert.notNull(productId, "产品id不能为空");
        Assert.notBlank(oldDeviceCode, "旧设备代码不能为空");
        Assert.notBlank(newDeviceCode, "新设备代码不能为空");
        try {
            tdEngineService.dropTable(Collections.singletonList(oldDeviceCode));
            tdEngineService.createTable(productId.toString(), newDeviceCode);
        } catch (Exception e) {
            throw new ServiceException("重命名表失败: {}", e.getMessage());
        }
    }

    /**
     * 查询最新的数据
     *
     * @param deviceCode 设备编码
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> selectLastData(String deviceCode) {
        if (StrUtil.isBlank(deviceCode)){
            return Collections.emptyMap();
        }
        try {
            return tdEngineService.selectLastData(deviceCode);
        } catch (Exception e) {
            log.warn("查询最新的数据失败: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}
