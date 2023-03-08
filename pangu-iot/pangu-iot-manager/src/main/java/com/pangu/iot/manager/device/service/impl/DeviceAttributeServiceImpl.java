package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.data.api.RemoteTdEngineService;
import com.pangu.iot.manager.device.convert.DeviceAttributeConvert;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.bo.LastDataAttributeBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import com.pangu.iot.manager.device.mapper.DeviceAttributeMapper;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.manager.api.domain.DriverAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 设备属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceAttributeServiceImpl extends ServiceImpl<DeviceAttributeMapper, DeviceAttribute> implements IDeviceAttributeService {

    private final IDeviceService deviceService;
    private final DeviceAttributeMapper baseMapper;
    private final RemoteTdEngineService tdEngineService;
    private final DeviceAttributeConvert deviceAttributeConvert;

    /**
     * 获取配置文件属性映射
     *
     * @param deviceIds 设备id
     * @return {@link Map}<{@link Long}, {@link Map}<{@link Long}, {@link DriverAttribute}>>
     */
    @Override
    public Map<Long, Map<Long, DeviceAttribute>> getProfileAttributeMap(Set<Long> deviceIds) {
        Map<Long, Map<Long, DeviceAttribute>> attributeVOMap = new ConcurrentHashMap<>(16);
        deviceIds.forEach(deviceId -> {
            List<DeviceAttributeVO> attributeVOList = this.queryVOListByDeviceId(deviceId);
            List<DeviceAttribute> deviceAttributes = deviceAttributeConvert.voListToEntityList(attributeVOList);
            attributeVOMap.put(deviceId, deviceAttributes.stream().collect(Collectors.toMap(DeviceAttribute::getId, attribute -> attribute)));
        });
        return attributeVOMap;
    }

    @Override

    public TableDataInfo<DeviceAttributeVO> queryLatestDataList(LastDataAttributeBO bo, PageQuery pageQuery) {
        // 查询设备信息
        Long deviceId = bo.getDeviceId();
        Device device = deviceService.getById(deviceId);
        Assert.notNull(device, "设备不存在");

        // 分页查询该设备所有属性
        Page<DeviceAttributeVO> attributeVOList = baseMapper.queryVOListByDeviceId(pageQuery.build(), ObjectUtil.isNull(bo.getProductId()) ? device.getProductId() : bo.getProductId(), deviceId);

        // 获取最新属性值并绑定
        Map<String, Object> lastRowData = tdEngineService.todayLastRowData(String.valueOf(device.getId()));
        attributeVOList.getRecords().forEach(attributeVO -> {
            attributeVO.setValue(lastRowData.get(attributeVO.getKey()));
            attributeVO.setClock(MapUtil.getStr(lastRowData, IotConstants.TABLE_PRIMARY_FIELD));
            attributeVO.setInherit(0 == attributeVO.getDeviceId());
        });
        return TableDataInfo.build(attributeVOList);
    }


    /**
     * 获取某设备所有属性包含产品属性
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DeviceAttributeVO}>
     */
    @Override
    public List<DeviceAttributeVO> queryVOListByDeviceId(Long deviceId) {
        Device device = deviceService.getById(deviceId);
        Assert.notNull(device, "设备不存在");
        List<DeviceAttributeVO> deviceAttributeVOList = baseMapper.queryVOListByDeviceId(device.getProductId(), deviceId);
        deviceAttributeVOList.forEach(deviceAttributeVO -> deviceAttributeVO.setInherit(0 == deviceAttributeVO.getDeviceId()));
        return deviceAttributeVOList;
    }


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
        lqw.eq(DeviceAttribute::getDeviceId, ObjectUtil.isNull(bo.getDeviceId()) ? 0 : bo.getDeviceId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeviceAttribute::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getKey()), DeviceAttribute::getKey, bo.getKey());
        lqw.eq(StringUtils.isNotBlank(bo.getValueType()), DeviceAttribute::getValueType, bo.getValueType());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), DeviceAttribute::getSource, bo.getSource());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), DeviceAttribute::getUnit, bo.getUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getMasterItemId()), DeviceAttribute::getMasterItemId, bo.getMasterItemId());
        lqw.eq(bo.getDependencyAttrId() != null, DeviceAttribute::getDependencyAttrId, bo.getDependencyAttrId());
        lqw.eq(StringUtils.isNotBlank(bo.getValueMapId()), DeviceAttribute::getValueMapId, bo.getValueMapId());
        return lqw;
    }

    @Override
    public Boolean existsProductAttributeBy(String key, Long productId) {
        LambdaQueryWrapper<DeviceAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeviceAttribute::getProductId, productId);
        lqw.eq(DeviceAttribute::getKey, key);
        return count(lqw) > 0;
    }

    /**
     * 新增设备属性
     */
    @Override
    public Boolean insertByBo(DeviceAttributeBO bo) {
        DeviceAttribute update = BeanUtil.toBean(bo, DeviceAttribute.class);
        validEntityBeforeSave(update);
        boolean flag = baseMapper.insert(update) > 0;
        if (flag) {
            bo.setId(update.getId());
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


    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByProductId(Long productId) {
        if (ObjectUtil.isNotNull(productId)){
            return false;
        }
        return baseMapper.delete(Wrappers.lambdaQuery(DeviceAttribute.class).eq(DeviceAttribute::getProductId, productId)) > 0;
    }


}
