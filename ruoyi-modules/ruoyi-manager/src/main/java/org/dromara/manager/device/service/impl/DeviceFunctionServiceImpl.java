package org.dromara.manager.device.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.dromara.manager.device.domain.bo.DeviceFunctionBo;
import org.dromara.manager.device.domain.vo.DeviceFunctionVo;
import org.dromara.manager.device.domain.DeviceFunction;
import org.dromara.manager.device.mapper.DeviceFunctionMapper;
import org.dromara.manager.device.service.IDeviceFunctionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 设备功能Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@RequiredArgsConstructor
@Service
public class DeviceFunctionServiceImpl implements IDeviceFunctionService {

    private final DeviceFunctionMapper baseMapper;

    /**
     * 查询设备功能
     */
    @Override
    public DeviceFunctionVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备功能列表
     */
    @Override
    public TableDataInfo<DeviceFunctionVo> queryPageList(DeviceFunctionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceFunction> lqw = buildQueryWrapper(bo);
        Page<DeviceFunctionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备功能列表
     */
    @Override
    public List<DeviceFunctionVo> queryList(DeviceFunctionBo bo) {
        LambdaQueryWrapper<DeviceFunction> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceFunction> buildQueryWrapper(DeviceFunctionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceFunction> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, DeviceFunction::getProductId, bo.getProductId());
        lqw.eq(bo.getDeviceId() != null, DeviceFunction::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getDriverId() != null, DeviceFunction::getDriverId, bo.getDriverId());
        lqw.eq(bo.getFunctionStatusAttribute() != null, DeviceFunction::getFunctionStatusAttribute, bo.getFunctionStatusAttribute());
        lqw.like(StringUtils.isNotBlank(bo.getFunctionName()), DeviceFunction::getFunctionName, bo.getFunctionName());
        lqw.like(StringUtils.isNotBlank(bo.getIdentifier()), DeviceFunction::getIdentifier, bo.getIdentifier());
        lqw.eq(StringUtils.isNotBlank(bo.getDataType()), DeviceFunction::getDataType, bo.getDataType());
        lqw.eq(bo.getAsync() != null, DeviceFunction::getAsync, bo.getAsync());
        return lqw;
    }

    /**
     * 新增设备功能
     */
    @Override
    public Boolean insertByBo(DeviceFunctionBo bo) {
        DeviceFunction add = MapstructUtils.convert(bo, DeviceFunction.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设备功能
     */
    @Override
    public Boolean updateByBo(DeviceFunctionBo bo) {
        DeviceFunction update = MapstructUtils.convert(bo, DeviceFunction.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceFunction entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备功能
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
