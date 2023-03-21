package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionRelationBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionRelationVO;
import com.pangu.iot.manager.device.domain.DeviceStatusFunctionRelation;
import com.pangu.iot.manager.device.mapper.DeviceStatusFunctionRelationMapper;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionRelationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备上下线规则与设备关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@RequiredArgsConstructor
@Service
public class DeviceStatusFunctionRelationServiceImpl extends ServiceImpl<DeviceStatusFunctionRelationMapper, DeviceStatusFunctionRelation> implements IDeviceStatusFunctionRelationService {

    private final DeviceStatusFunctionRelationMapper baseMapper;

    /**
     * 查询设备上下线规则与设备关系
     */
    @Override
    public DeviceStatusFunctionRelationVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备上下线规则与设备关系列表
     */
    @Override
    public TableDataInfo<DeviceStatusFunctionRelationVO> queryPageList(DeviceStatusFunctionRelationBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceStatusFunctionRelation> lqw = buildQueryWrapper(bo);
        Page<DeviceStatusFunctionRelationVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备上下线规则与设备关系列表
     */
    @Override
    public List<DeviceStatusFunctionRelationVO> queryList(DeviceStatusFunctionRelationBO bo) {
        LambdaQueryWrapper<DeviceStatusFunctionRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceStatusFunctionRelation> buildQueryWrapper(DeviceStatusFunctionRelationBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceStatusFunctionRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getRuleId() != null, DeviceStatusFunctionRelation::getRuleId, bo.getRuleId());
        lqw.eq(bo.getRelationId() != null, DeviceStatusFunctionRelation::getRelationId, bo.getRelationId());
        lqw.eq(bo.getZbxId() != null, DeviceStatusFunctionRelation::getZbxId, bo.getZbxId());
        lqw.eq(bo.getZbxIdRecovery() != null, DeviceStatusFunctionRelation::getZbxIdRecovery, bo.getZbxIdRecovery());
        return lqw;
    }

    /**
     * 新增设备上下线规则与设备关系
     */
    @Override
    public Boolean insertByBo(DeviceStatusFunctionRelationBO bo) {
        DeviceStatusFunctionRelation add = BeanUtil.toBean(bo, DeviceStatusFunctionRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备上下线规则与设备关系
     */
    @Override
    public Boolean updateByBo(DeviceStatusFunctionRelationBO bo) {
        DeviceStatusFunctionRelation update = BeanUtil.toBean(bo, DeviceStatusFunctionRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceStatusFunctionRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备上下线规则与设备关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
