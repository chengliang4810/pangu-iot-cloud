package org.dromara.manager.device.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.DeviceAttribute;
import org.dromara.manager.device.domain.bo.DeviceAttributeBo;
import org.dromara.manager.device.domain.vo.DeviceAttributeVo;
import org.dromara.manager.device.mapper.DeviceAttributeMapper;
import org.dromara.manager.device.service.IDeviceAttributeService;
import org.springframework.stereotype.Service;

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

    private final DeviceAttributeMapper baseMapper;

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
        lqw.like(StringUtils.isNotBlank(bo.getAttributeName()), DeviceAttribute::getAttributeName, bo.getAttributeName());
        lqw.eq(StringUtils.isNotBlank(bo.getIdentifier()), DeviceAttribute::getIdentifier, bo.getIdentifier());
        return lqw;
    }

    /**
     * 新增设备属性
     */
    @Override
    public Boolean insertByBo(DeviceAttributeBo bo) {
        DeviceAttribute add = MapstructUtils.convert(bo, DeviceAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
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
        Assert.isFalse(exists, "标识符{}重复", identifier);
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
}
