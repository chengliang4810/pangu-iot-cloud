package org.dromara.manager.device.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.data.api.RemoteTableService;
import org.dromara.manager.device.domain.DeviceAttribute;
import org.dromara.manager.device.domain.bo.DeviceAttributeBo;
import org.dromara.manager.device.domain.vo.DeviceAttributeVo;
import org.dromara.manager.device.mapper.DeviceAttributeMapper;
import org.dromara.manager.device.service.IDeviceAttributeService;
import org.dromara.manager.driver.domain.bo.PointAttributeValueBo;
import org.dromara.manager.driver.service.IPointAttributeValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设备属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@RequiredArgsConstructor
@Service
public class DeviceAttributeServiceImpl implements IDeviceAttributeService {

    @DubboReference
    private RemoteTableService remoteTableService;
    private final DeviceAttributeMapper baseMapper;
    private final IPointAttributeValueService pointAttributeValueService;

    /**
     * 查询设备属性
     */
    @Override
    public DeviceAttributeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备属性列表
     */
    @Override
    public TableDataInfo<DeviceAttributeVo> queryPageList(DeviceAttributeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceAttribute> lqw = buildQueryWrapper(bo);
        Page<DeviceAttributeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备属性列表
     */
    @Override
    public List<DeviceAttributeVo> queryList(DeviceAttributeBo bo) {
        LambdaQueryWrapper<DeviceAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceAttribute> buildQueryWrapper(DeviceAttributeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, DeviceAttribute::getId, bo.getId());
        lqw.in(bo.getDeviceId() != null, DeviceAttribute::getDeviceId, bo.getDeviceId(), 0);
        lqw.eq(bo.getProductId() != null, DeviceAttribute::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getAttributeName()), DeviceAttribute::getAttributeName, bo.getAttributeName());
        lqw.eq(StringUtils.isNotBlank(bo.getIdentifier()), DeviceAttribute::getIdentifier, bo.getIdentifier());
        return lqw;
    }

    /**
     * 新增设备属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(DeviceAttributeBo bo) {
        DeviceAttribute add = MapstructUtils.convert(bo, DeviceAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {

            // 添加点位属性配置
            savePointAttributeValue(bo);
            // 更新TdEngine表结构
            addTableField(bo);

            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 添加表字段
     *
     * @param bo 薄
     */
    private void addTableField(DeviceAttributeBo bo) {
        if (ObjectUtil.isNotNull(bo.getDeviceId()) && 0 != bo.getDeviceId()) {
            // 有设备id则为设备属性，不需要添加表字段
            return;
        }
        remoteTableService.addSuperTableField(bo.getProductId(), bo.getIdentifier(), bo.getAttributeType());
    }

    /**
     * 保存点位属性值
     *
     * @param bo 设备信息
     */
    private void savePointAttributeValue(DeviceAttributeBo bo) {
        if (ObjectUtil.isNull(bo.getDeviceId()) || 0 == bo.getDeviceId()) {
            // 无设备id忽略， 一般是产品属性，则无需保存点位属性值
            return;
        }
        Map<Long, String> pointAttributeConfig = bo.getPointAttributeConfig();
        if (CollUtil.isEmpty(pointAttributeConfig)){
            // 无点位属性配置忽略
            return;
        }

        pointAttributeConfig.forEach((attributeId, value) -> {
            PointAttributeValueBo valueBo = new PointAttributeValueBo();
            valueBo.setDeviceId(bo.getDeviceId());
            valueBo.setDeviceAttributeId(bo.getId());
            valueBo.setPointAttributeId(attributeId);
            valueBo.setValue(value);
            pointAttributeValueService.insertByBo(valueBo);
        });

    }

    /**
     * 修改设备属性
     */
    @Override
    public Boolean updateByBo(DeviceAttributeBo bo) {
        DeviceAttribute update = MapstructUtils.convert(bo, DeviceAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceAttribute entity){
        //TODO 做一些数据校验,如唯一约束
        String identifier = entity.getIdentifier();
        boolean exists = baseMapper.exists(Wrappers.lambdaQuery(DeviceAttribute.class)
            .eq(DeviceAttribute::getProductId, entity.getProductId())
            .eq(DeviceAttribute::getIdentifier, identifier)
        );
        Assert.isFalse(exists, "标识符{}已被使用", identifier);
    }

    /**
     * 批量删除设备属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 查询设备id 属性对应的列表
     *
     * @param bo
     * @return {@link List}<{@link DeviceAttributeVo}>
     */
    @Override
    public List<DeviceAttributeVo> queryListByProductIdAndDeviceId(DeviceAttributeBo bo) {
        Assert.notNull(bo.getProductId(), "产品id不能为空");
        Assert.notNull(bo.getDeviceId(), "设备id不能为空");
        return baseMapper.selectVoList( Wrappers.lambdaQuery(DeviceAttribute.class)
            .eq(DeviceAttribute::getProductId, bo.getProductId())
            .in(DeviceAttribute::getDeviceId, bo.getDeviceId(), 0)
        );
    }


    /**
     * 根据设备id仅删除设备属性
     *
     * @param deviceId id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByDeviceId(Long deviceId) {
        Assert.notNull(deviceId, "设备id不能为空");
        return baseMapper.delete(Wrappers.lambdaQuery(DeviceAttribute.class)
            .eq(DeviceAttribute::getDeviceId, deviceId)
        ) > 0;
    }

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByProductId(Long productId) {
        return baseMapper.delete(Wrappers.lambdaQuery(DeviceAttribute.class)
            .eq(DeviceAttribute::getProductId, productId)
        ) > 0;
    }
}
