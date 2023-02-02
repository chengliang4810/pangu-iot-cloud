package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusBO;
import com.pangu.iot.manager.device.domain.vo.DeviceDetailVO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import com.pangu.iot.manager.device.mapper.DeviceMapper;
import com.pangu.iot.manager.device.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设备Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final DeviceMapper baseMapper;

    /**
     * 查询设备
     */
    @Override
    public DeviceDetailVO queryById(Long id){
        return baseMapper.detailById(id);
    }

    /**
     * 查询设备列表
     */
    @Override
    public TableDataInfo<DeviceListVO> queryPageList(DeviceBO bo, PageQuery pageQuery) {
        QueryWrapper<Device> lqw = buildWrapper(bo);
        Page<DeviceListVO> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备列表
     */
    @Override
    public List<DeviceVO> queryList(DeviceBO bo) {
        LambdaQueryWrapper<Device> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Device> buildQueryWrapper(DeviceBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Device> lqw = Wrappers.lambdaQuery(Device.class);
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Device::getCode, bo.getCode());
        // lqw.in(CollectionUtil.isNotEmpty(bo.getGroupIds()), Device::getGroupId, bo.getGroupIds());
        lqw.eq(bo.getProductId() != null, Device::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Device::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Device::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), Device::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getPosition()), Device::getPosition, bo.getPosition());
        return lqw;
    }

    private QueryWrapper<Device> buildWrapper(DeviceBO bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<Device> lqw = Wrappers.query();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Device.CONST_CODE, bo.getCode());
        lqw.in(CollectionUtil.isNotEmpty(bo.getGroupIds()), DeviceGroupRelation.CONST_DEVICE_GROUP_ID, bo.getGroupIds());
        lqw.in(CollectionUtil.isNotEmpty(bo.getProductIds()), Device.CONST_PRODUCT_ID, bo.getProductIds());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Device.CONST_NAME, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Device.CONST_TYPE, bo.getType());
        return lqw;
    }

    /**
     * 新增设备
     */
    @Override
    public Boolean insertByBo(DeviceBO bo) {
        Device add = BeanUtil.toBean(bo, Device.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备
     */
    @Override
    public Boolean updateByBo(DeviceBO bo) {
        Device update = BeanUtil.toBean(bo, Device.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Device entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 更新设备状态
     *
     * @param deviceStatusBO 设备状态
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateDeviceStatus(DeviceStatusBO deviceStatusBO) {
        return baseMapper.updateById(new Device().setStatus(deviceStatusBO.getStatus()).setId(deviceStatusBO.getDeviceId())) > 0;
    }

}