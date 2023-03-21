package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.convert.DeviceStatusFunctionConvert;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionVO;
import com.pangu.iot.manager.device.mapper.DeviceStatusFunctionMapper;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设备上下线规则Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@RequiredArgsConstructor
@Service
public class DeviceStatusFunctionServiceImpl extends ServiceImpl<DeviceStatusFunctionMapper, DeviceStatusFunction> implements IDeviceStatusFunctionService {

    private final DeviceStatusFunctionMapper baseMapper;
    private final DeviceStatusFunctionConvert deviceStatusFunctionConvert;
    private final IDeviceAttributeService deviceAttributeService;

    /**
     * 查询设备上下线规则
     */
    @Override
    public DeviceStatusFunctionVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备上下线规则列表
     */
    @Override
    public TableDataInfo<DeviceStatusFunctionVO> queryPageList(DeviceStatusFunctionBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceStatusFunction> lqw = buildQueryWrapper(bo);
        Page<DeviceStatusFunctionVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备上下线规则列表
     */
    @Override
    public List<DeviceStatusFunctionVO> queryList(DeviceStatusFunctionBO bo) {
        LambdaQueryWrapper<DeviceStatusFunction> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceStatusFunction> buildQueryWrapper(DeviceStatusFunctionBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceStatusFunction> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAttributeId() != null, DeviceStatusFunction::getAttributeId, bo.getAttributeId());
        lqw.eq(StringUtils.isNotBlank(bo.getRuleFunction()), DeviceStatusFunction::getRuleFunction, bo.getRuleFunction());
        lqw.eq(StringUtils.isNotBlank(bo.getRuleCondition()), DeviceStatusFunction::getRuleCondition, bo.getRuleCondition());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), DeviceStatusFunction::getUnit, bo.getUnit());
        lqw.eq(bo.getAttributeIdRecovery() != null, DeviceStatusFunction::getAttributeIdRecovery, bo.getAttributeIdRecovery());
        lqw.eq(StringUtils.isNotBlank(bo.getRuleFunctionRecovery()), DeviceStatusFunction::getRuleFunctionRecovery, bo.getRuleFunctionRecovery());
        lqw.eq(StringUtils.isNotBlank(bo.getRuleConditionRecovery()), DeviceStatusFunction::getRuleConditionRecovery, bo.getRuleConditionRecovery());
        lqw.eq(StringUtils.isNotBlank(bo.getUnitRecovery()), DeviceStatusFunction::getUnitRecovery, bo.getUnitRecovery());
        lqw.eq(bo.getStatus() != null, DeviceStatusFunction::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增设备上下线规则
     */
    @Override
    public Boolean insertByBo(DeviceStatusFunctionBO bo) {
        DeviceStatusFunction add = BeanUtil.toBean(bo, DeviceStatusFunction.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备上下线规则
     */
    @Override
    public Boolean updateByBo(DeviceStatusFunctionBO bo) {
        DeviceStatusFunction update = BeanUtil.toBean(bo, DeviceStatusFunction.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceStatusFunction entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备上下线规则
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 查询关系id
     *
     * @param id id
     * @return {@link DeviceStatusFunctionVO}
     */
    @Override
    public DeviceStatusFunctionVO queryRelationId(Long id) {
        DeviceStatusFunction deviceStatusFunction = baseMapper.selectByRelationId(id);
        if (ObjectUtil.isNull(deviceStatusFunction)) {
            return null;
        }
        DeviceStatusFunctionVO statusFunctionVO = deviceStatusFunctionConvert.toVo(deviceStatusFunction);
        // 查询上线属性名称与下线属性名称
        DeviceAttribute deviceAttribute = deviceAttributeService.getById(deviceStatusFunction.getAttributeId());
        DeviceAttribute deviceAttributeRecovery = deviceAttributeService.getById(deviceStatusFunction.getAttributeIdRecovery());
        if (ObjectUtil.isNotNull(deviceAttribute)) {
            statusFunctionVO.setAttributeName(deviceAttribute.getName());
        }
        if (ObjectUtil.isNotNull(deviceAttributeRecovery)) {
            statusFunctionVO.setAttributeNameRecovery(deviceAttributeRecovery.getName());
        }
        return statusFunctionVO;
    }


}
