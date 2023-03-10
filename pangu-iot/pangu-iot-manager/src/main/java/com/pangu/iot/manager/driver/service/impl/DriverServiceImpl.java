package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.driver.convert.DriverAttributeConvert;
import com.pangu.iot.manager.driver.convert.DriverConvert;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.bo.DriverBO;
import com.pangu.iot.manager.driver.domain.bo.DriverServiceBO;
import com.pangu.iot.manager.driver.domain.vo.DriverAttributeVO;
import com.pangu.iot.manager.driver.domain.vo.DriverConfigVO;
import com.pangu.iot.manager.driver.domain.vo.DriverServiceVO;
import com.pangu.iot.manager.driver.domain.vo.DriverVO;
import com.pangu.iot.manager.driver.mapper.DriverMapper;
import com.pangu.iot.manager.driver.service.IDriverAttributeService;
import com.pangu.iot.manager.driver.service.IDriverInfoService;
import com.pangu.iot.manager.driver.service.IDriverService;
import com.pangu.iot.manager.driver.service.IDriverServiceService;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DriverAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.CommonConstant.Symbol.COMMA;

/**
 * ????????????Service???????????????
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {

    private final DriverMapper baseMapper;
    private final DriverConvert driverConvert;
    private final IDeviceService deviceService;
    private final IProductService productService;
    private final IDriverInfoService driverInfoService;
    private final IDriverServiceService driverServiceService;
    private final DriverAttributeConvert driverAttributeConvert;
    private final IDriverAttributeService driverAttributeService;

    @Override
    public List<DriverConfigVO> getDriverConfigByDeviceId(Long deviceId) {
        // ??????
        Device device = deviceService.getById(deviceId);
        if (device == null) {
            return null;
        }
        // ??????
        Product product = productService.getById(device.getProductId());
        if (product == null) {
            return null;
        }
        // ??????ID
        String driverIds = product.getDriver();
        if (StrUtil.isBlank(driverIds)) {
            return null;
        }

        // ??????????????????
        List<Driver> driverList = baseMapper.selectList(Wrappers.lambdaQuery(Driver.class).in(Driver::getId, driverIds.split(COMMA)));
        if (CollectionUtil.isEmpty(driverList)) {
            return null;
        }

        List<DriverConfigVO> driverConfigList = new ArrayList<>(driverList.size());
        // ???????????????????????????????????????
        driverList.forEach(driver -> {
            DriverConfigVO driverVO = driverConvert.toConfigVo(driver);

            // ??????????????????
            List<DriverAttribute> driverAttributeList = driverAttributeService.list(Wrappers.lambdaQuery(DriverAttribute.class).eq(DriverAttribute::getDriverId, driver.getId()));

            // ???????????????
            List<Long> attributeIds = driverAttributeList.stream().map(DriverAttribute::getId).collect(Collectors.toList());
            Map<Long, String> attributeValueMap = driverInfoService.getDriverInfoValueMap(deviceId, attributeIds);

            // ???????????????
            List<DriverAttributeVO> driverAttributeVOList = driverAttributeConvert.toVoList(driverAttributeList);
            driverAttributeVOList.forEach(driverAttributeVO -> {
                driverAttributeVO.setValue(attributeValueMap.get(driverAttributeVO.getId()));
            });

            driverVO.setAttributeList(driverAttributeVOList);
            driverConfigList.add(driverVO);
        });

        return driverConfigList;
    }


    @Override
    public Driver selectByName(String name) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(Driver.class).eq(Driver::getName, name).last("limit 1"));
    }

    /**
     * ??????????????????
     */
    @Override
    public DriverVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * ????????????????????????
     */
    @Override
    public TableDataInfo<DriverVO> queryPageList(DriverBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        Page<Driver> driverPage = baseMapper.selectPage(pageQuery.build(), lqw);
        List<DriverVO> voList = driverConvert.toVoList(driverPage.getRecords());

        // ???????????????????????????????????????????????????
        voList.forEach(vo ->{
            List<String> serviceIds = getDriverServiceIds(vo.getName());
            vo.setServerNumber(serviceIds.size());
            List<DriverServiceVO> serviceVOList = driverServiceService.queryList(new DriverServiceBO().setDriverId(vo.getId()));
            serviceVOList.stream().filter(serviceVO -> serviceIds.contains(serviceVO.getId())).forEach(serviceVO -> serviceVO.setOnlineStatus(true));
            vo.setServiceList(serviceVOList);
        });
        return TableDataInfo.build(voList, driverPage.getTotal());
    }

    /**
     * ????????????????????????Id
     *
     * @param driverName ????????????
     * @return {@link List}<{@link String}>
     */
    private List<String> getDriverServiceIds(String driverName) {
        List<String> keys = RedisUtils.getCacheKeyNumber(IotConstants.RedisKey.DRIVER_HEARTBEAT + driverName + "*");
        return keys.stream().map(key -> key.replace(IotConstants.RedisKey.DRIVER_HEARTBEAT, "")).map(SecureUtil::md5).collect(Collectors.toList());
    }

    /**
     * ????????????????????????
     */
    @Override
    public List<DriverVO> queryList(DriverBO bo) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Driver> buildQueryWrapper(DriverBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Driver> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Driver::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), Driver::getDisplayName, bo.getDisplayName());
        lqw.eq(bo.getEnable() != null, Driver::getEnable, bo.getEnable());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), Driver::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * ??????????????????
     */
    @Override
    public Boolean insertByBo(DriverBO bo) {
        Driver add = BeanUtil.toBean(bo, Driver.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * ??????????????????
     */
    @Override
    public Boolean updateByBo(DriverBO bo) {
        Driver update = BeanUtil.toBean(bo, Driver.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * ????????????????????????
     */
    private void validEntityBeforeSave(Driver entity){
        //TODO ?????????????????????,???????????????
    }

    /**
     * ????????????????????????
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO ???????????????????????????,????????????????????????
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
