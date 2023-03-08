package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.domain.bo.PointInfoBO;
import com.pangu.iot.manager.driver.domain.bo.PointInfoBatchBO;
import com.pangu.iot.manager.driver.domain.vo.PointInfoVO;
import com.pangu.iot.manager.driver.mapper.PointInfoMapper;
import com.pangu.iot.manager.driver.service.IPointInfoService;
import com.pangu.iot.manager.driver.service.event.DriverEvent;
import com.pangu.manager.api.domain.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.manager.api.domain.PointAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 点位属性配置信息Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PointInfoServiceImpl extends ServiceImpl<PointInfoMapper, PointInfo> implements IPointInfoService {

    private final PointInfoMapper baseMapper;
    private final IDeviceService deviceService;
    private final ApplicationContext applicationContext;
    private final IDeviceAttributeService deviceAttributeService;


    @Override
    public Map<Long, Map<Long, Map<String, AttributeInfo>>> getPointInfoMap(List<Device> devices, Map<Long, Map<Long, DeviceAttribute>> profileAttributeMap, Map<Long, PointAttribute> pointAttributeMap) {
        Map<Long, Map<Long, Map<String, AttributeInfo>>> devicePointInfoMap = new ConcurrentHashMap<>(16);
        devices.forEach(device -> {
            Map<Long, Map<String, AttributeInfo>> infoMap = getPointInfoMap(device, profileAttributeMap, pointAttributeMap);
            if (infoMap.size() > 0) {
                devicePointInfoMap.put(device.getId(), infoMap);
            }
        });
        return devicePointInfoMap;
    }


    /**
     * Get point info map
     *
     * @param device            Device
     * @param profilePointMap   Profile Point Map
     * @param pointAttributeMap Point Attribute Map
     * @return map(pointId, attribute ( attributeName, attributeInfo ( value, type)))
     */
    public Map<Long, Map<String, AttributeInfo>> getPointInfoMap(Device device, Map<Long, Map<Long, DeviceAttribute>> profilePointMap, Map<Long, PointAttribute> pointAttributeMap) {
        Map<Long, Map<String, AttributeInfo>> attributeInfoMap = new ConcurrentHashMap<>(16);
        profilePointMap.values().forEach(deviceAttributeMap -> deviceAttributeMap.keySet()
            .forEach(attributeId -> {
                List<PointInfo> pointInfos = this.selectByDeviceIdAndAttributeId(device.getId(), attributeId);

                Map<String, AttributeInfo> infoMap = new ConcurrentHashMap<>(16);
                pointInfos.forEach(pointInfo -> {
                    PointAttribute attribute = pointAttributeMap.get(pointInfo.getPointAttributeId());
                    infoMap.put(attribute.getName(), new AttributeInfo(pointInfo.getValue(), attribute.getType()));
                });

                if (infoMap.size() > 0) {
                    attributeInfoMap.put(attributeId, infoMap);
                }
            }));
        return attributeInfoMap;
    }

    /**
     * 查询设备点位属性配置信息列表
     * @param deviceId
     * @param attributeId
     * @return
     */
    private List<PointInfo> selectByDeviceIdAndAttributeId(Long deviceId, Long attributeId) {
        List<PointInfo> pointInfos = baseMapper.selectList(Wrappers.lambdaQuery(PointInfo.class)
            .eq(PointInfo::getDeviceId, deviceId)
            .eq(PointInfo::getDeviceAttributeId, attributeId));
        return pointInfos;
    }


    /**
     * 批量更新
     *
     * @param bo 薄
     * @return {@link Boolean}
     */
    @Override
    public Boolean batchUpdate(PointInfoBatchBO bo) {
        Long deviceId = bo.getDeviceId();
        // 验证设备是否存在
        Device device = deviceService.getById(deviceId);
        Assert.notNull(device, "设备不存在");
        Long deviceAttributeId = bo.getDeviceAttributeId();
        // 验证属性是否存在
        DeviceAttribute deviceAttribute = deviceAttributeService.getById(deviceAttributeId);
        Assert.notNull(deviceAttribute, "属性不存在");

        Map<Long, String> attributeValue = bo.getAttributeValue();
        List<PointInfo> pointInfoList = new ArrayList<PointInfo>(attributeValue.size());

        attributeValue.forEach((attributeId,value) -> {
            // 查询驱动配置信息
            PointInfo dbPointInfo = baseMapper.selectOne(Wrappers.lambdaQuery(PointInfo.class)
                    .eq(PointInfo::getDeviceId, deviceId)
                    .eq(PointInfo::getPointAttributeId, attributeId)
                    .eq(PointInfo::getDeviceAttributeId, deviceAttributeId)
                    .last("limit 1")
            );

            // 构建驱动配置信息
            PointInfo pointInfo = new PointInfo();
            pointInfo.setDeviceId(deviceId);
            pointInfo.setDeviceAttributeId(deviceAttributeId);
            pointInfo.setPointAttributeId(attributeId);
            pointInfo.setValue(value);
            // 数据库存在则更新，不存在则插入 通过主键判断
            if (ObjectUtil.isNotNull(dbPointInfo)){
                pointInfo.setId(dbPointInfo.getId());
            }
            pointInfoList.add(pointInfo);
        });

        boolean b = baseMapper.insertOrUpdateBatch(pointInfoList);
        if (b) {
            // 通知驱动
            applicationContext.publishEvent(new DriverEvent(this));
        }
        return b;
    }

    /**
     * 得到点信息价值地图
     *
     * @param deviceId    设备id
     * @param attributeId
     * @param pointIds    点id
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    @Override
    public Map<Long, String> getPointInfoValueMap(Long deviceId, Long attributeId, Set<Long> pointIds) {
        List<PointInfo> driverInfos = baseMapper.selectList(Wrappers.lambdaQuery(PointInfo.class)
                .eq(PointInfo::getDeviceId, deviceId)
                .eq(PointInfo::getDeviceAttributeId, attributeId)
                .in(PointInfo::getPointAttributeId, pointIds));
        if (CollectionUtil.isEmpty(driverInfos)) {
            return Collections.emptyMap();
        }
        return driverInfos.stream().collect(Collectors.toMap(PointInfo::getPointAttributeId, PointInfo::getValue));
    }

    @Override
    public List<PointInfo> selectByAttributeId(Long pointAttributeId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(PointInfo.class).eq(PointInfo::getPointAttributeId, pointAttributeId));
    }

    /**
     * 查询点位属性配置信息
     */
    @Override
    public PointInfoVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点位属性配置信息列表
     */
    @Override
    public TableDataInfo<PointInfoVO> queryPageList(PointInfoBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointInfo> lqw = buildQueryWrapper(bo);
        Page<PointInfoVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询点位属性配置信息列表
     */
    @Override
    public List<PointInfoVO> queryList(PointInfoBO bo) {
        LambdaQueryWrapper<PointInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointInfo> buildQueryWrapper(PointInfoBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPointAttributeId() != null, PointInfo::getPointAttributeId, bo.getPointAttributeId());
        lqw.eq(bo.getDeviceId() != null, PointInfo::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getDeviceAttributeId() != null, PointInfo::getDeviceAttributeId, bo.getDeviceAttributeId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), PointInfo::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), PointInfo::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增点位属性配置信息
     */
    @Override
    public Boolean insertByBo(PointInfoBO bo) {
        PointInfo add = BeanUtil.toBean(bo, PointInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改点位属性配置信息
     */
    @Override
    public Boolean updateByBo(PointInfoBO bo) {
        PointInfo update = BeanUtil.toBean(bo, PointInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除点位属性配置信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
