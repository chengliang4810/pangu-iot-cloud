package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import com.pangu.iot.manager.device.domain.DeviceAttribute;
import com.pangu.iot.manager.device.mapper.DeviceAttributeMapper;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@RequiredArgsConstructor
@Service
public class DeviceAttributeServiceImpl extends ServiceImpl<DeviceAttributeMapper, DeviceAttribute> implements IDeviceAttributeService {

    private final DeviceAttributeMapper baseMapper;

    /**
     * 查询设备属性
     */
    @Override
    public DeviceAttributeVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备属性列表
     */
    @Override
    public TableDataInfo<DeviceAttributeVO> queryPageList(DeviceAttributeBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceAttribute> lqw = buildQueryWrapper(bo);
        Page<DeviceAttributeVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备属性列表
     */
    @Override
    public List<DeviceAttributeVO> queryList(DeviceAttributeBO bo) {
        LambdaQueryWrapper<DeviceAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceAttribute> buildQueryWrapper(DeviceAttributeBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, DeviceAttribute::getProductId, bo.getProductId());
        lqw.eq(bo.getDeviceId() != null, DeviceAttribute::getDeviceId, bo.getDeviceId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeviceAttribute::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getKey()), DeviceAttribute::getKey, bo.getKey());
        lqw.eq(StringUtils.isNotBlank(bo.getValueType()), DeviceAttribute::getValueType, bo.getValueType());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), DeviceAttribute::getSource, bo.getSource());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), DeviceAttribute::getUnit, bo.getUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getMasterItemId()), DeviceAttribute::getMasterItemId, bo.getMasterItemId());
        lqw.eq(bo.getDependencyAttrId() != null, DeviceAttribute::getDependencyAttrId, bo.getDependencyAttrId());
        lqw.eq(StringUtils.isNotBlank(bo.getZbxId()), DeviceAttribute::getZbxId, bo.getZbxId());
        lqw.eq(StringUtils.isNotBlank(bo.getTemplateId()), DeviceAttribute::getTemplateId, bo.getTemplateId());
        lqw.eq(StringUtils.isNotBlank(bo.getValueMapId()), DeviceAttribute::getValueMapId, bo.getValueMapId());
        return lqw;
    }

    /**
     * 新增设备属性
     */
    @Override
    public Boolean insertByBo(DeviceAttributeBO bo) {
        DeviceAttribute add = BeanUtil.toBean(bo, DeviceAttribute.class);
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
    public Boolean updateByBo(DeviceAttributeBO bo) {
        DeviceAttribute update = BeanUtil.toBean(bo, DeviceAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceAttribute entity){
        //TODO 做一些数据校验,如唯一约束
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
