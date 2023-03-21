package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupRelationBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupRelationVO;
import com.pangu.iot.manager.device.mapper.DeviceGroupRelationMapper;
import com.pangu.iot.manager.device.service.IDeviceGroupRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设备与分组关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-07
 */
@RequiredArgsConstructor
@Service
public class DeviceGroupRelationServiceImpl extends ServiceImpl<DeviceGroupRelationMapper, DeviceGroupRelation> implements IDeviceGroupRelationService {

    private final DeviceGroupRelationMapper baseMapper;

    /**
     * 查询设备组列表
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DeviceGroup}>
     */
    @Override
    public List<DeviceGroup> queryDeviceGroupListByDeviceId(Long deviceId) {
        return baseMapper.selectDeviceGroupListByDeviceId(deviceId);
    }


    /**
     * 查询设备与分组关系
     */
    @Override
    public DeviceGroupRelationVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备与分组关系列表
     */
    @Override
    public TableDataInfo<DeviceGroupRelationVO> queryPageList(DeviceGroupRelationBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceGroupRelation> lqw = buildQueryWrapper(bo);
        Page<DeviceGroupRelationVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备与分组关系列表
     */
    @Override
    public List<DeviceGroupRelationVO> queryList(DeviceGroupRelationBO bo) {
        LambdaQueryWrapper<DeviceGroupRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceGroupRelation> buildQueryWrapper(DeviceGroupRelationBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceGroupRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDeviceId() != null, DeviceGroupRelation::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getDeviceGroupId() != null, DeviceGroupRelation::getDeviceGroupId, bo.getDeviceGroupId());
        return lqw;
    }

    /**
     * 新增设备与分组关系
     */
    @Override
    public Boolean insertByBo(DeviceGroupRelationBO bo) {
        DeviceGroupRelation add = BeanUtil.toBean(bo, DeviceGroupRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备与分组关系
     */
    @Override
    public Boolean updateByBo(DeviceGroupRelationBO bo) {
        DeviceGroupRelation update = BeanUtil.toBean(bo, DeviceGroupRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceGroupRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备与分组关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
