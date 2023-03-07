package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.driver.convert.DriverConvert;
import com.pangu.iot.manager.driver.convert.PointAttributeConvert;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.bo.PointAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.DriverPointConfigVO;
import com.pangu.iot.manager.driver.domain.vo.PointAttributeVO;
import com.pangu.iot.manager.driver.mapper.PointAttributeMapper;
import com.pangu.iot.manager.driver.service.IDriverService;
import com.pangu.iot.manager.driver.service.IPointAttributeService;
import com.pangu.iot.manager.driver.service.IPointInfoService;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.manager.api.domain.PointAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.CommonConstant.Symbol.COMMA;

/**
 * 点位属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class PointAttributeServiceImpl extends ServiceImpl<PointAttributeMapper, PointAttribute> implements IPointAttributeService {

    private final PointAttributeMapper baseMapper;
    private final IDeviceService deviceService;
    private final IProductService productService;
    private final IDriverService driverService;
    private final IPointInfoService pointInfoService;
    private final DriverConvert driverConvert;
    private final PointAttributeConvert pointAttributeConvert;


    @Override
    public Map<Long, Map<Long, PointAttribute>> getProfilePointMap(Set<Long> deviceIds) {
        Map<Long, Map<Long, PointAttribute>> profilePointMap = new ConcurrentHashMap<>(16);
//        deviceIds.forEach(deviceId -> {
//            Set<String> profileIds = profileBindService.selectProfileIdsByDeviceId(deviceId);
//            profileIds.forEach(profileId -> profilePointMap.put(profileId, getPointMap(profileId)));
//        });
        return profilePointMap;
    }

    /**
     * 得到点属性映射
     *
     * @param driverId 司机身份证
     * @return {@link Map}<{@link Long}, {@link DriverAttribute}>
     */
    @Override
    public Map<Long, PointAttribute> getPointAttributeMap(Long driverId) {
        List<PointAttribute> pointAttributeList = baseMapper.selectList(Wrappers.lambdaQuery(PointAttribute.class).eq(PointAttribute::getDriverId, driverId));
        return pointAttributeList.stream().collect(Collectors.toMap(PointAttribute::getId, pointAttribute -> pointAttribute));
    }

    /**
     * 司机配置设备id
     *
     * @param deviceId    设备id
     * @param attributeId
     * @return {@link List}<{@link DriverPointConfigVO}>
     */
    @Override
    public List<DriverPointConfigVO> getDriverPointConfigByDeviceId(Long deviceId, Long attributeId) {

        // 设备
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            return null;
        }
        // 产品
        Product product = productService.getById(device.getProductId());
        if (product == null) {
            return null;
        }
        // 驱动ID
        String driverIds = product.getDriver();
        if (StrUtil.isBlank(driverIds)) {
            return null;
        }

        // 查询驱动信息
        List<Driver> driverList = driverService.list(Wrappers.lambdaQuery(Driver.class).in(Driver::getId, driverIds.split(COMMA)));
        if (CollectionUtil.isEmpty(driverList)) {
            return null;
        }

        // 查询点位属性
        List<PointAttribute> pointAttributeList = baseMapper.selectList(Wrappers.lambdaQuery(PointAttribute.class).in(PointAttribute::getDriverId, driverIds.split(COMMA)));
        if (CollectionUtil.isEmpty(pointAttributeList)) {
            return null;
        }

        List<DriverPointConfigVO> driverPointConfigList = new ArrayList<>(driverList.size());

        // 查询驱动对应属性以及属性值
        driverList.forEach(driver -> {
            DriverPointConfigVO driverPointConfigVO = driverConvert.toPointConfigVo(driver);

            // 查询所有点位属性
            List<PointAttribute> pointAttributes = baseMapper.selectList(Wrappers.lambdaQuery(PointAttribute.class).eq(PointAttribute::getDriverId, driver.getId()));

            // 查询属性值
            Set<Long> pointIds = pointAttributes.stream().map(PointAttribute::getId).collect(Collectors.toSet());
            Map<Long, String> attributeValueMap = pointInfoService.getPointInfoValueMap(deviceId, attributeId, pointIds);

            // 组装属性值
            List<PointAttributeVO> driverAttributeVOList = pointAttributeConvert.toVoList(pointAttributes);
            driverAttributeVOList.forEach(driverAttributeVO -> {
                driverAttributeVO.setValue(attributeValueMap.get(driverAttributeVO.getId()));
            });

            driverPointConfigVO.setAttributeList(driverAttributeVOList);
            driverPointConfigList.add(driverPointConfigVO);

        });
        return driverPointConfigList;
    }


    @Override
    public List<PointAttribute> selectByDriverId(Long driverId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(PointAttribute.class).eq(PointAttribute::getDriverId, driverId));
    }

    /**
     * 查询点位属性
     */
    @Override
    public PointAttributeVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点位属性列表
     */
    @Override
    public TableDataInfo<PointAttributeVO> queryPageList(PointAttributeBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        Page<PointAttributeVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询点位属性列表
     */
    @Override
    public List<PointAttributeVO> queryList(PointAttributeBO bo) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointAttribute> buildQueryWrapper(PointAttributeBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, PointAttribute::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), PointAttribute::getDisplayName, bo.getDisplayName());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PointAttribute::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), PointAttribute::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), PointAttribute::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), PointAttribute::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增点位属性
     */
    @Override
    public Boolean insertByBo(PointAttributeBO bo) {
        PointAttribute add = BeanUtil.toBean(bo, PointAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改点位属性
     */
    @Override
    public Boolean updateByBo(PointAttributeBO bo) {
        PointAttribute update = BeanUtil.toBean(bo, PointAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointAttribute entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除点位属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
