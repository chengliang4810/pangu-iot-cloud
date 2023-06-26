package org.dromara.manager.device.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.Device;
import org.dromara.manager.device.domain.bo.DeviceBo;
import org.dromara.manager.device.domain.vo.DeviceVo;
import org.dromara.manager.device.mapper.DeviceMapper;
import org.dromara.manager.device.service.IDeviceService;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 设备Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl implements IDeviceService {

    private final DeviceMapper baseMapper;
    private final IProductService productService;

    /**
     * 通过驱动ID查询设备列表
     *
     * @param driverId
     * @param enabled
     */
    @Override
    public List<DeviceVo> queryDeviceListByDriverId(Long driverId, Boolean enabled) {
        if (ObjUtil.isNull(driverId)) {
            return Collections.emptyList();
        }
        return baseMapper.selectDeviceListByDriverId(driverId, enabled);
    }

    /**
     * 查询设备
     */
    @Override
    public DeviceVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备列表
     */
    @Override
    public TableDataInfo<DeviceVo> queryPageList(DeviceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Device> lqw = buildQueryWrapper(bo);
        Page<DeviceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备列表
     */
    @Override
    public List<DeviceVo> queryList(DeviceBo bo) {
        LambdaQueryWrapper<Device> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Device> buildQueryWrapper(DeviceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Device> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGroupId() != null, Device::getGroupId, bo.getGroupId());
        lqw.eq(bo.getProductId() != null, Device::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Device::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Device::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, Device::getStatus, bo.getStatus());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            Device::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增设备
     */
    @Override
    public Boolean insertByBo(DeviceBo bo) {
        ProductVo productVo = productService.queryById(bo.getProductId());
        if (ObjUtil.isNull(productVo)) {
            throw new ServiceException("产品不存在");
        }
        bo.setDeviceType(productVo.getType());
        Device add = MapstructUtils.convert(bo, Device.class);
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
    public Boolean updateByBo(DeviceBo bo) {
        Device update = MapstructUtils.convert(bo, Device.class);
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
     * 查询设备通过网关id列表
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DeviceVo}>
     */
    @Override
    public List<DeviceVo> queryChildDeviceListByDeviceId(Long deviceId, Boolean enabled) {
        return baseMapper.selectChildDeviceListByDeviceId(deviceId, enabled);
    }


}
