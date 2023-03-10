package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.common.satoken.utils.LoginHelper;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.data.api.RemoteDeviceStatusService;
import com.pangu.iot.manager.device.convert.DeviceConvert;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.GatewayDeviceBind;
import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.bo.DeviceGatewayBindBo;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusBO;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteBO;
import com.pangu.iot.manager.device.domain.vo.DeviceDetailVO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import com.pangu.iot.manager.device.mapper.DeviceMapper;
import com.pangu.iot.manager.device.service.IDeviceGroupRelationService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.device.service.IGatewayDeviceBindService;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import com.pangu.iot.manager.product.service.IProductServiceParamService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.ProductService;
import com.pangu.system.api.model.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.DEVICE_CODE_CACHE_PREFIX;

/**
 * ??????Service???????????????
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @DubboReference
    private final RemoteDeviceStatusService deviceStatusService;

    private final DeviceMapper baseMapper;
    private final EmqxClient emqxClient;
    private final DeviceConvert deviceConvert;
    private final IGatewayDeviceBindService gatewayDeviceBindService;
    private final IProductServiceService productServiceService;
    private final IProductServiceParamService productServiceParamService;
    private final IProductEventServiceService productEventServiceService;
    private final IDeviceGroupRelationService deviceGroupRelationService;
    private final IServiceExecuteRecordService serviceExecuteRecordService;

    /**
     * ??????????????????????????????id
     *
     * @param id id
     * @return {@link List}<{@link Long}>
     */
    @Override
    public List<Long> queryGatewayDeviceBindIds(Long id) {
        return gatewayDeviceBindService.list(Wrappers.<GatewayDeviceBind>lambdaQuery().eq(GatewayDeviceBind::getGatewayDeviceId, id)).stream().distinct().map(GatewayDeviceBind::getDeviceId).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean bindGatewayDevice(DeviceGatewayBindBo deviceGatewayBindBo) {
        Long gatewayDeviceId = deviceGatewayBindBo.getGatewayDeviceId();
        List<Long> deviceIds = deviceGatewayBindBo.getDeviceIds();

        Device gatewayDevice = getById(gatewayDeviceId);
        Assert.notNull(gatewayDevice, "?????????????????????");

        // ????????????????????????
        gatewayDeviceBindService.remove(Wrappers.<GatewayDeviceBind>lambdaQuery().eq(GatewayDeviceBind::getGatewayDeviceId, gatewayDeviceId));

        // ??????????????????
        List<GatewayDeviceBind> gatewayDeviceBinds = new ArrayList<>();
        deviceIds.forEach(deviceId -> {
            GatewayDeviceBind gatewayDeviceBind = new GatewayDeviceBind();
            gatewayDeviceBind.setGatewayDeviceId(gatewayDeviceId);
            gatewayDeviceBind.setDeviceId(deviceId);
            gatewayDeviceBinds.add(gatewayDeviceBind);
        });

        return gatewayDeviceBindService.saveBatch(gatewayDeviceBinds);
    }

    /**
     * ????????????ID??????code
     *
     * @param code
     */
    @Override
    public Long queryDeviceIdByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        // ?????????
        Long cacheId = RedisUtils.getCacheObject(DEVICE_CODE_CACHE_PREFIX + code);
        if (ObjectUtil.isNotNull(cacheId)) {
            return cacheId;
        }

        // ????????????
        Long id = baseMapper.selectDeviceIdByCode(code);
        if (ObjectUtil.isNotNull(id)) {
            RedisUtils.setCacheObject(DEVICE_CODE_CACHE_PREFIX + code, id);
        }

        return id;
    }


    /**
     * ????????????
     */
    @Override
    public DeviceDetailVO queryById(Long id){
        return baseMapper.detailById(id);
    }

    /**
     * ??????????????????
     */
    @Override
    public TableDataInfo<DeviceListVO> queryPageList(DeviceBO bo, PageQuery pageQuery) {
        QueryWrapper<Device> lqw = buildWrapper(bo);
        Page<DeviceListVO> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        // ??????????????????
        buildDeviceStatus(result.getRecords());

        return TableDataInfo.build(result);
    }

    /**
     * ????????????
     *
     * @param records ??????
     */
    private void buildDeviceStatus(List<DeviceListVO> records) {
        Set<Long> ids = records.stream().map(DeviceListVO::getId).collect(Collectors.toSet());
        Map<Long, Integer> deviceStatus = deviceStatusService.getDeviceOnlineStatus(ids);
        records.forEach(item -> {
            Integer status = deviceStatus.get(item.getId());
            item.setOnline(ObjectUtil.isNotNull(status));
        });
    }

    /**
     * ??????????????????
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
        lqw.eq(ObjectUtil.isNotNull(bo.getProductId()), Device.CONST_PRODUCT_ID, bo.getProductId());
        lqw.in(CollectionUtil.isNotEmpty(bo.getGroupIds()), DeviceGroupRelation.CONST_DEVICE_GROUP_ID, bo.getGroupIds());
        lqw.in(CollectionUtil.isNotEmpty(bo.getProductIds()), Device.CONST_PRODUCT_ID, bo.getProductIds());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Device.CONST_NAME, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Device.CONST_TYPE, bo.getType());
        lqw.in(ObjectUtil.isNotNull(bo.getGatewayDeviceId()), Device.CONST_GATEWAY_DEVICE, bo.getGatewayDeviceId());
        return lqw;
    }

    /**
     * ????????????
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
     * ????????????
     */
    @Override
    @Transactional
    public Boolean updateByBo(DeviceBO bo) {
        Device device = deviceConvert.toEntity(bo);

        // ???????????????????????????
        if (CollectionUtil.isNotEmpty(bo.getGroupIds())) {
            // ????????????????????????
            deviceGroupRelationService.remove(Wrappers.<DeviceGroupRelation>lambdaQuery().eq(DeviceGroupRelation::getDeviceId, device.getId()));
            // ??????????????????
            List<DeviceGroupRelation> relations = bo.getGroupIds().stream().map(groupId -> {
                DeviceGroupRelation relation = new DeviceGroupRelation();
                relation.setDeviceId(device.getId());
                relation.setDeviceGroupId(groupId);
                return relation;
            }).collect(Collectors.toList());
            deviceGroupRelationService.saveBatch(relations);
        }

        return baseMapper.updateById(device) > 0;
    }

    /**
     * ????????????????????????
     */
    private void validEntityBeforeSave(Device entity){
        //TODO ?????????????????????,???????????????
    }

    /**
     * ??????????????????
     */
    @Override
    public Integer deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO ???????????????????????????,????????????????????????
        }
        return baseMapper.deleteBatchIds(ids);
    }

    /**
     * ??????????????????
     *
     * @param deviceStatusBO ????????????
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateDeviceStatus(DeviceStatusBO deviceStatusBO) {
        return baseMapper.updateById(new Device().setStatus(deviceStatusBO.getStatus()).setId(deviceStatusBO.getDeviceId())) > 0;
    }

    /**
     * ????????????
     *
     * @param deviceId ??????id
     * @param eventId  ?????????
     */
    @Override
    public void executeService(Long deviceId, Long eventId, Integer executeType) {

        // ??????????????????ID
        ProductEventService service = productEventServiceService.getOne(Wrappers.<ProductEventService>lambdaQuery().eq(ProductEventService::getEventRuleId, eventId));
        Long serviceId = service.getServiceId();

        // ????????????
        List<ProductServiceParam> paramList = productServiceParamService.list(Wrappers.<ProductServiceParam>lambdaQuery().eq(ProductServiceParam::getServiceId, serviceId));
//        List<ServiceExecuteBO.ServiceParam> serviceParams = new ArrayList<>();
//        paramList.forEach(param -> {
//            ServiceExecuteBO.ServiceParam serviceParam = new ServiceExecuteBO.ServiceParam();
//            serviceParam.setKey(param.getKey());
//            serviceParam.setValue(param.getValue());
//            serviceParams.add(serviceParam);
//        });

        // ??????
 //       executeService(deviceId, serviceId, serviceParams, executeType);
    }

    @Override
    public void executeService(ServiceExecuteBO serviceExecute) {
        Long deviceId = serviceExecute.getDeviceId();
        Long serviceId = serviceExecute.getServiceId();
        Object value = serviceExecute.getValue();
        long startTime = System.currentTimeMillis();
        boolean executeStatus = true;
        // ??????????????????
        Device device = getById(deviceId);
        if (ObjectUtil.isNull(device)){
            executeStatus = false;
        }
        ProductService productService = productServiceService.getById(serviceId);
        if (null == productService) {
            executeStatus = false;
        }

        Map<String, String> paramStr = Collections.singletonMap(productService.getMark(), JsonUtils.toJsonString(value));

        long snowflakeNextId = IdUtil.getSnowflakeNextId();
        //???????????? ??????
        if (executeStatus){
            DeviceFunction deviceFunction = new DeviceFunction();
            deviceFunction.setDeviceId(deviceId);
            deviceFunction.setServiceId(serviceId);
            deviceFunction.setIdentifier(productService.getMark());
            deviceFunction.setUuid(snowflakeNextId);
            deviceFunction.setValue(new AttributeInfo(value, productService.getDataType().name()));
            String topic = "iot/device/" + deviceId + "/function/" + productService.getMark() + "/exec";
            emqxClient.publish(topic, JsonUtils.toJsonString(deviceFunction), 2);
            log.info("?????????????????????topic???{}????????????{}", topic, JsonUtils.toJsonString(deviceFunction));

            if (productService.getAsync() == 0){
                // ????????????
                String result = RedisUtils.getCacheObject("iot:device:" + deviceId + ":function:" + productService.getMark() + ":" + deviceFunction.getUuid());
                for (int i = 0; i < 30; i++) {
                    if (StrUtil.isNotBlank(result)) {
                        break;
                    }
                    ThreadUtil.sleep(100);
                    result = RedisUtils.getCacheObject("iot:device:" + deviceId + ":function:" + productService.getMark() + ":" + deviceFunction.getUuid());
                }

                executeStatus = StrUtil.isNotBlank(result) && "success".equals(result);
                RedisUtils.deleteObject("iot:device:" + deviceId + ":function:" + productService.getMark() + ":" + deviceFunction.getUuid());

            } else {
                executeStatus = false;
            }
        }

        //??????????????????
        ServiceExecuteRecord serviceExecuteRecord = new ServiceExecuteRecord();
        serviceExecuteRecord.setDeviceId(deviceId);
        if (CollectionUtil.isNotEmpty(paramStr)) {
            serviceExecuteRecord.setParam(JsonUtils.toJsonString(paramStr));
        }

        ProductService service = productServiceService.getById(serviceId);
        serviceExecuteRecord.setId(snowflakeNextId);
        serviceExecuteRecord.setServiceName(service.getName());
        serviceExecuteRecord.setCreateTime(new Date());
        serviceExecuteRecord.setExecuteType(1);
        serviceExecuteRecord.setExecuteUser(getLoginUsername());
        serviceExecuteRecord.setExecuteStatus(executeStatus);
        serviceExecuteRecordService.save(serviceExecuteRecord);

        log.info("?????????????????????{}ms", System.currentTimeMillis() - startTime);


    }

    /**
     * ?????????????????????
     */
    private String getLoginUsername() {
        LoginUser loginUser;
        try {
            loginUser = LoginHelper.getLoginUser();
        } catch (Exception e) {
            return "system";
        }
        return loginUser.getUsername();
    }




}
