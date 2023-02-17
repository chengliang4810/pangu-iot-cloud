package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.common.satoken.utils.LoginHelper;
import com.pangu.data.api.RemoteDeviceStatusService;
import com.pangu.iot.manager.device.convert.DeviceConvert;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusBO;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteBO;
import com.pangu.iot.manager.device.domain.vo.DeviceDetailVO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import com.pangu.iot.manager.device.mapper.DeviceMapper;
import com.pangu.iot.manager.device.service.IDeviceGroupRelationService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.ProductService;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import com.pangu.iot.manager.product.service.IProductServiceParamService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import com.pangu.system.api.model.LoginUser;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.DEVICE_CODE_CACHE_PREFIX;

/**
 * 设备Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    @DubboReference
    private final RemoteDeviceStatusService deviceStatusService;

    private final DeviceMapper baseMapper;
    private final DeviceConvert deviceConvert;
    private final IProductServiceService productServiceService;
    private final IProductServiceParamService productServiceParamService;
    private final IProductEventServiceService productEventServiceService;
    private final IDeviceGroupRelationService deviceGroupRelationService;
    private final IServiceExecuteRecordService serviceExecuteRecordService;

    /**
     * 获取设备ID使用code
     *
     * @param code
     */
    @Override
    public Long queryDeviceIdByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }

        // 取缓存
        Long cacheId = RedisUtils.getCacheObject(DEVICE_CODE_CACHE_PREFIX + code);
        if (ObjectUtil.isNotNull(cacheId)) {
            return cacheId;
        }

        // 添加缓存
        Long id = baseMapper.selectDeviceIdByCode(code);
        if (ObjectUtil.isNotNull(id)) {
            RedisUtils.setCacheObject(DEVICE_CODE_CACHE_PREFIX + code, id);
        }

        return id;
    }


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
        // 查询设备状态
        buildDeviceStatus(result.getRecords());

        return TableDataInfo.build(result);
    }

    /**
     * 设备状态
     *
     * @param records 记录
     */
    private void buildDeviceStatus(List<DeviceListVO> records) {
        Set<String> ids = records.stream().map(DeviceListVO::getCode).collect(Collectors.toSet());
        Map<String, Integer> deviceStatus = deviceStatusService.getDeviceOnlineStatus(ids);
        System.out.println("设备状态：" + deviceStatus);
        records.forEach(item -> {
            Integer status = deviceStatus.get(item.getCode());
            item.setOnline(ObjectUtil.isNotNull(status));
        });
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
        lqw.eq(ObjectUtil.isNotNull(bo.getProductId()), Device.CONST_PRODUCT_ID, bo.getProductId());
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
    @Transactional
    public Boolean updateByBo(DeviceBO bo) {
        Device device = deviceConvert.toEntity(bo);

        // 更新设备组关联关系
        if (CollectionUtil.isNotEmpty(bo.getGroupIds())) {
            // 删除原有关联关系
            deviceGroupRelationService.remove(Wrappers.<DeviceGroupRelation>lambdaQuery().eq(DeviceGroupRelation::getDeviceId, device.getId()));
            // 新增关联关系
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
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Device entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备
     */
    @Override
    public Integer deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids);
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

    /**
     * 执行功能
     *
     * @param deviceId 设备id
     * @param eventId  标识符
     */
    @Override
    public void executeService(Long deviceId, Long eventId, Integer executeType) {

        // 获取对应功能ID
        ProductEventService service = productEventServiceService.getOne(Wrappers.<ProductEventService>lambdaQuery().eq(ProductEventService::getEventRuleId, eventId));
        Long serviceId = service.getServiceId();

        // 构建参数
        List<ProductServiceParam> paramList = productServiceParamService.list(Wrappers.<ProductServiceParam>lambdaQuery().eq(ProductServiceParam::getServiceId, serviceId));
        List<ServiceExecuteBO.ServiceParam> serviceParams = new ArrayList<>();
        paramList.forEach(param -> {
            ServiceExecuteBO.ServiceParam serviceParam = new ServiceExecuteBO.ServiceParam();
            serviceParam.setKey(param.getKey());
            serviceParam.setValue(param.getValue());
            serviceParams.add(serviceParam);
        });

        // 执行
        executeService(deviceId, serviceId, serviceParams, executeType);
    }

    @Override
    public void executeService(Long deviceId, Long serviceId, List<ServiceExecuteBO.ServiceParam> serviceParams, Integer executeType) {
        boolean executeStatus = true;
        Assert.notNull(executeType, "执行类型不能为空");
        // 查询设备信息
        Device device = getById(deviceId);
        if (ObjectUtil.isNull(device)){
            executeStatus = false;
        }
        //封装执行参数
//        List<Map<String, Object>> body = new ArrayList<>();
//
//        Map<String, Object> map = new ConcurrentHashMap<>(2);
//
//        map.put("device", deviceId);
//
//        List<Map<String, Object>> serviceList = new ArrayList<>();
//        Map<String, Object> serviceMap = new ConcurrentHashMap<>(2);
//        ProductService productService = new QProductService().id.eq(serviceId).findOne();
//        if (null == productService) {
//            throw new ServiceException(BizExceptionEnum.SERVICE_NOT_EXISTS);
//        }
//        serviceMap.put("name", productService.getName());
//

        List<ProductServiceParam> paramList = productServiceParamService.list(Wrappers.<ProductServiceParam>lambdaQuery().eq(ProductServiceParam::getServiceId, serviceId));
        Map<String, String> paramStr = null;
        if (CollectionUtil.isNotEmpty(paramList)) {
            paramStr = paramList.parallelStream().collect(Collectors.toMap(ProductServiceParam::getKey, ProductServiceParam::getValue, (a, b) -> a));

            if (CollectionUtil.isNotEmpty(serviceParams)) {
                Map<String, String> userParam = serviceParams.parallelStream().collect(Collectors.toMap(ServiceExecuteBO.ServiceParam::getKey, ServiceExecuteBO.ServiceParam::getValue, (a, b) -> a));
                for (Map.Entry<String, String> param : paramStr.entrySet()) {
                    if (userParam.get(param.getKey()) != null) {
                        param.setValue(userParam.get(param.getKey()));
                    }
                }
            }

            // serviceMap.put("param", paramStr);
        }

//        serviceList.add(serviceMap);
//        map.put("service", serviceList);
//        body.add(map);

        //下发命令 执行
        // Forest.post("/device/action/exec").host("127.0.0.1").port(12800).contentTypeJson().addBody(JSON.toJSON(body)).execute();

        //记录服务日志
        ServiceExecuteRecord serviceExecuteRecord = new ServiceExecuteRecord();
        serviceExecuteRecord.setDeviceId(deviceId);
        if (CollectionUtil.isNotEmpty(paramStr)) {
            serviceExecuteRecord.setParam(JsonUtils.toJsonString(paramStr));
        }

        ProductService service = productServiceService.getById(serviceId);
        serviceExecuteRecord.setServiceName(service.getName());
        serviceExecuteRecord.setCreateTime(new Date());
        serviceExecuteRecord.setExecuteType(executeType);
        serviceExecuteRecord.setExecuteUser(getLoginUsername());
        serviceExecuteRecord.setExecuteStatus(executeStatus);
        serviceExecuteRecordService.save(serviceExecuteRecord);
    }

    /**
     * 获取登录用户名
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
