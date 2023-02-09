package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.service.TriggerService;
import com.pangu.iot.manager.device.domain.bo.DeviceEventRuleBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAlarmRuleVO;
import com.pangu.iot.manager.device.service.IDeviceEventRuleService;
import com.pangu.iot.manager.product.convert.ProductEventConvert;
import com.pangu.iot.manager.product.convert.ProductEventExpressionConvert;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import com.pangu.iot.manager.product.service.IProductEventRelationService;
import com.pangu.iot.manager.product.service.IProductEventService;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.ALARM_TAG_NAME;
import static com.pangu.common.core.constant.IotConstants.EXECUTE_TAG_NAME;

/**
 * 设备告警规则Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Service
@RequiredArgsConstructor
public class DeviceEventRuleServiceImpl implements IDeviceEventRuleService {

    private final TriggerService triggerService;
    private final ProductEventConvert productEventConvert;
    private final IProductEventService productEventService;
    private final IProductEventServiceService productEventServiceService;
    private final IProductEventRelationService productEventRelationService;
    private final ProductEventExpressionConvert productEventExpressionConvert;
    private final IProductEventExpressionService productEventExpressionService;


    @Override
    public DeviceAlarmRuleVO getById(Long id) {
        // 查询规则是否存在
        ProductEvent productEvent = productEventService.getById(id);
        Assert.notNull(productEvent, "告警规则不存在");

        DeviceAlarmRuleVO deviceAlarmRuleVO = productEventConvert.toDeviceVO(productEvent);
        // 是否继承自产品
        ProductEventRelation eventRelation = productEventRelationService.getOne(Wrappers.lambdaQuery(ProductEventRelation.class).eq(ProductEventRelation::getEventRuleId, id), false);
        deviceAlarmRuleVO.setInherit(eventRelation != null && eventRelation.getInherit() > 0);

        // 规则表达式
        List<ProductEventExpression> expressionList = productEventExpressionService.list(Wrappers.lambdaQuery(ProductEventExpression.class).eq(ProductEventExpression::getRuleId, id));
        deviceAlarmRuleVO.setExpList(expressionList);

        // 规则服务
        List<ProductEventService> productEventServiceList = productEventServiceService.list(Wrappers.lambdaQuery(ProductEventService.class).eq(ProductEventService::getEventRuleId, id));
        List<DeviceAlarmRuleVO.DeviceService> deviceServiceList = productEventServiceList.stream().map(productEventService -> {
            DeviceAlarmRuleVO.DeviceService deviceService = new DeviceAlarmRuleVO.DeviceService();
            deviceService.setServiceId(productEventService.getServiceId());
            deviceService.setDeviceId(productEventService.getRelationId());
            return deviceService;
        }).collect(Collectors.toList());
        deviceAlarmRuleVO.setDeviceServices(deviceServiceList);

        return deviceAlarmRuleVO;
    }

    /**
     * 检查动作服务是否重复
     *
     * @param deviceServices
     */
    private void checkService(List<DeviceEventRuleBO.DeviceService> deviceServices) {
        if (CollectionUtil.isEmpty(deviceServices)) {
            return;
        }
        long count = deviceServices.parallelStream().map(DeviceEventRuleBO.DeviceService::getServiceId).distinct().count();
        Assert.isFalse(count < deviceServices.size(), "存在相同的动作服务");
    }

    /**
     * 创建设备告警规则
     *
     * @param deviceEventRule 设备告警规则信息
     * @return {@link Boolean}
     */
    @Override
    @Transactional
    public Boolean createDeviceEventRule(DeviceEventRuleBO deviceEventRule) {
        //检查是否有重复动作服务
        checkService(deviceEventRule.getDeviceServices());

        List<Long> deviceIds = deviceEventRule.getExpList().parallelStream().map(DeviceEventRuleBO.Expression::getDeviceId).distinct().collect(Collectors.toList());
        Assert.notEmpty(deviceIds, "告警规则缺少关联产品或设备");

        // 生成唯一ID
        Long eventRuleId = IdUtil.getSnowflakeNextId();

        // 触发器 Tag
        Map<String, String> tags = new ConcurrentHashMap<>(3);
        if (CollectionUtil.isNotEmpty(deviceEventRule.getTags())) {
            tags = deviceEventRule.getTags().stream().collect(Collectors.toMap(DeviceEventRuleBO.Tag::getTag, DeviceEventRuleBO.Tag::getValue, (k1, k2) -> k2));
        }
        if (!tags.containsKey(ALARM_TAG_NAME)) {
            tags.put(ALARM_TAG_NAME, "{HOST.HOST}");
        }
        if (CollectionUtil.isNotEmpty(deviceEventRule.getDeviceServices()) && !tags.containsKey(EXECUTE_TAG_NAME)) {
            tags.put(EXECUTE_TAG_NAME, eventRuleId.toString());
        }

        // 创建 zbx 触发器
        String expression = deviceEventRule.getExpList().stream().map(Object::toString).collect(Collectors.joining(" " + deviceEventRule.getExpLogic() + " "));
        String triggerId = triggerService.createZbxTrigger(eventRuleId + "", expression, deviceEventRule.getEventLevel(), tags);

        // 保存设备告警规则
        ProductEvent productEvent = productEventConvert.toEntity(deviceEventRule);
        productEvent.setId(eventRuleId);
        productEventService.save(productEvent);

        //step 2: 保存 表达式，方便回显
        List<ProductEventExpression> expList = new ArrayList<>();
        deviceEventRule.getExpList().forEach(i -> {
            ProductEventExpression exp = productEventExpressionConvert.toEntity(i);
            exp.setRuleId(eventRuleId);
            expList.add(exp);
        });
        productEventExpressionService.saveBatch(expList);

        //step 3: 保存触发器 调用 动作服务
        if (CollectionUtil.isNotEmpty(deviceEventRule.getDeviceServices())) {
            List<ProductEventService> productEventServiceList = new ArrayList<>();
            deviceIds.forEach(deviceId -> {
                    deviceEventRule.getDeviceServices().forEach(deviceService -> {
                        ProductEventService productEventService = new ProductEventService();
                        productEventService.setEventRuleId(eventRuleId);
                        productEventService.setServiceId(deviceService.getServiceId());
                        productEventService.setRelationId(deviceId);
                        productEventService.setExecuteDeviceId(deviceService.getExecuteDeviceId());
                        productEventServiceList.add(productEventService);
                    });
            });
            productEventServiceService.saveBatch(productEventServiceList);
        }

        //step 4: 保存关联关系

        List<ProductEventRelation> productEventRelationList = new ArrayList<>();
        deviceIds.forEach(relationId -> {
            ProductEventRelation productEventRelation = new ProductEventRelation(eventRuleId, relationId, triggerId, deviceEventRule.getRemark());

            productEventRelation.setInherit(0L);
            productEventRelationList.add(productEventRelation);
        });

        return  productEventRelationService.saveBatch(productEventRelationList);
    }


}
