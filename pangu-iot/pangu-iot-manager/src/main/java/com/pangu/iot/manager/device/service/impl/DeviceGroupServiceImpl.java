package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupVO;
import com.pangu.iot.manager.device.mapper.DeviceGroupMapper;
import com.pangu.iot.manager.device.service.IDeviceGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设备分组Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@RequiredArgsConstructor
@Service
public class DeviceGroupServiceImpl implements IDeviceGroupService {

    private final DeviceGroupMapper baseMapper;

    /**
     * 查询设备分组
     */
    @Override
    public DeviceGroupVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备分组列表
     */
    @Override
    public TableDataInfo<DeviceGroupVO> queryPageList(DeviceGroupBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceGroup> lqw = buildQueryWrapper(bo);
        Page<DeviceGroupVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备分组列表
     */
    @Override
    public List<DeviceGroupVO> queryList(DeviceGroupBO bo) {
        LambdaQueryWrapper<DeviceGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceGroup> buildQueryWrapper(DeviceGroupBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeviceGroup::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getZbxId()), DeviceGroup::getZbxId, bo.getZbxId());
        return lqw;
    }

    /**
     * 新增设备分组
     */
    @Override
    public Boolean insertByBo(DeviceGroupBO bo) {
        DeviceGroup add = BeanUtil.toBean(bo, DeviceGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备分组
     */
    @Override
    public Boolean updateByBo(DeviceGroupBO bo) {
        DeviceGroup update = BeanUtil.toBean(bo, DeviceGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备分组
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
