package org.dromara.manager.device.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.Device;
import org.dromara.manager.device.domain.bo.ChildDeviceBo;
import org.dromara.manager.device.domain.bo.DeviceBo;
import org.dromara.manager.device.domain.bo.GatewayBindRelationBo;
import org.dromara.manager.device.domain.vo.DeviceVo;
import org.dromara.manager.device.mapper.DeviceMapper;
import org.dromara.manager.device.service.IDeviceService;
import org.dromara.manager.device.service.IGatewayBindRelationService;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final IGatewayBindRelationService gatewayBindRelationService;

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
    public DeviceVo queryById(Long id) {
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
        lqw.eq(bo.getDeviceType() != null, Device::getDeviceType, bo.getDeviceType());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Device::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Device::getName, bo.getName());
        lqw.eq(bo.getStatus() != null, Device::getStatus, bo.getStatus());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            Device::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增设备
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(DeviceBo bo) {
        ProductVo productVo = productService.queryById(bo.getProductId());
        Assert.notNull(productVo, "产品不存在");
        // 与产品关联的设备类型一致
        bo.setDeviceType(productVo.getType());
        Device add = MapstructUtils.convert(bo, Device.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            // 更新产品设备数量
            productService.updateDeviceNumber(bo.getProductId(), 1);
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
    private void validEntityBeforeSave(Device entity) {
        //TODO 做一些数据校验,如唯一约束
        Assert.notNull(entity, "设备不能为空");
        // 设备编码唯一
        String code = entity.getCode();
        boolean exists = baseMapper.exists(Wrappers.lambdaQuery(Device.class)
            .eq(Device::getCode, code).ne(entity.getId() != null, Device::getId, entity.getId()));
        Assert.isFalse(exists, "设备编码已存在");

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

    /**
     * 数子设备id
     *
     * @param id id
     * @param enabled  enabled
     * @return {@link Long}
     */
    @Override
    public Long countChildByDeviceId(Long id, Boolean enabled) {
        return baseMapper.countChildByDeviceId(id, enabled);
    }

    /**
     * 根据ID删除设备信息
     *
     * @param deviceId 设备id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteById(Long deviceId) {
        return baseMapper.deleteById(deviceId) > 0;
    }

    /**
     * 添加子设备
     *
     * @param bo 薄
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addChildDevice(ChildDeviceBo bo) {
        Long deviceId = bo.getDeviceId();
        DeviceVo deviceVo = queryById(deviceId);

        Assert.notNull(deviceVo, "设备不存在");
        Assert.isTrue(deviceVo.getDeviceType().equals(2), "非网关设备无法绑定子设备");

        AtomicInteger count = new AtomicInteger(0);

        bo.getChildDeviceIds().forEach(childDeviceId -> {
            Boolean existed = gatewayBindRelationService.existChildDevice(deviceId, childDeviceId);
            if (existed) {
                return;
            }
            Boolean aBoolean = gatewayBindRelationService.insertByBo(new GatewayBindRelationBo().setDeviceId(childDeviceId).setGatewayDeviceId(deviceId));
            if (aBoolean) {
                count.incrementAndGet();
            }
        });

        return count.get();
    }

    /**
     * 根据产品id统计设备数量
     *
     * @param id id
     * @return {@link Long}
     */
    @Override
    public Long countDeviceByProductId(Long id) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(Device.class).eq(Device::getProductId, id));
    }
}
