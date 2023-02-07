package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.enums.SeverityEnum;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.zabbix.service.TriggerService;
import com.pangu.iot.manager.product.convert.ProductEventConvert;
import com.pangu.iot.manager.product.convert.ProductEventExpressionConvert;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.bo.ProductEventBO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventRuleVO;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import com.pangu.iot.manager.product.mapper.ProductEventMapper;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import com.pangu.iot.manager.product.service.IProductEventRelationService;
import com.pangu.iot.manager.product.service.IProductEventService;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.ALARM_TAG_NAME;
import static com.pangu.common.core.constant.IotConstants.EXECUTE_TAG_NAME;

/**
 * 告警规则Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@RequiredArgsConstructor
@Service
public class ProductEventServiceImpl extends ServiceImpl<ProductEventMapper, ProductEvent> implements IProductEventService {

    private final ProductEventMapper baseMapper;
    private final TriggerService triggerService;
    private final ProductEventConvert productEventConvert;
    private final IProductEventServiceService eventServiceService;
    private final IProductEventRelationService productEventRelationService;
    private final ProductEventExpressionConvert productEventExpressionConvert;
    private final IProductEventExpressionService productEventExpressionService;


    /**
     * 更新产品事件规则id
     *
     * @param bo 薄
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProductEventRuleById(ProductEventRuleBO bo) {
        // 查询规则是否存在
        ProductEvent event = baseMapper.selectById(bo.getEventRuleId());
        Assert.notNull(event, "告警规则不存在");

        //step 1: 删除关联的函数表达式
        productEventExpressionService.remove(Wrappers.lambdaQuery(ProductEventExpression.class).eq(ProductEventExpression::getRuleId, bo.getEventRuleId()));

        //step 2: 删除关联的服务方法调用
        eventServiceService.remove(Wrappers.lambdaQuery(ProductEventService.class).eq(ProductEventService::getEventRuleId, bo.getEventRuleId()));

        // step 3: 更新产品告警规则
        ProductEvent productEvent = productEventConvert.toEntity(bo);
        int result = baseMapper.updateById(productEvent);

        // step 4: 保存 表达式，方便回显
        List<ProductEventExpression> expList = new ArrayList<>();
        bo.getExpList().forEach(exp -> {
            ProductEventExpression productEventExpression = productEventExpressionConvert.toEntity(exp);
            productEventExpression.setRuleId(bo.getEventRuleId());
            expList.add(productEventExpression);
        });
        productEventExpressionService.saveBatch(expList);

        // step 5: 保存服务方法调用
        if (CollectionUtil.isNotEmpty(bo.getDeviceServices())) {
            List<ProductEventService> productEventServiceList = new ArrayList<>();
            bo.getDeviceServices().forEach(deviceService -> {
                ProductEventService productEventService = new ProductEventService();
                productEventService.setEventRuleId(bo.getEventRuleId());
                productEventService.setServiceId(deviceService.getServiceId());
                productEventService.setRelationId(bo.getProductId());
                productEventService.setExecuteDeviceId(0L);
                productEventServiceList.add(productEventService);
            });
            eventServiceService.saveBatch(productEventServiceList);
        }

        // 更新zbx表达式
        String expression = bo.getExpList().stream().map(Object::toString).collect(Collectors.joining(" " + bo.getExpLogic() + " "));
        ProductEventRelation eventRelation = productEventRelationService.getOne(Wrappers.lambdaQuery(ProductEventRelation.class).eq(ProductEventRelation::getEventRuleId, bo.getEventRuleId()).last(" limit 1"));

        triggerService.triggerUpdate(eventRelation.getZbxId(), expression,  bo.getEventLevel());

        return result > 0;
    }

    /**
     * 查询产品活动规则
     *
     * @param id id
     * @return {@link ProductEventRuleVO}
     */
    @Override
    public ProductEventRuleVO queryProductEventRule(Long id) {

        // 查询规则是否存在
        ProductEvent productEvent = this.getById(id);
        Assert.notNull(productEvent, "告警规则不存在");

        ProductEventRuleVO productEventRuleVO = productEventConvert.toVO(productEvent);

        // 规则表达式
        List<ProductEventExpression> productEventExpressionList = productEventExpressionService.list(Wrappers.lambdaQuery(ProductEventExpression.class).eq(ProductEventExpression::getRuleId, id));
        productEventRuleVO.setExpList(productEventExpressionList);

        // 规则服务
        List<ProductEventService> productEventServiceList = eventServiceService.list(Wrappers.lambdaQuery(ProductEventService.class).eq(ProductEventService::getEventRuleId, id));
        List<ProductEventRuleVO.DeviceService> deviceServiceList = productEventServiceList.stream().map(productEventService -> {
            ProductEventRuleVO.DeviceService deviceService = new ProductEventRuleVO.DeviceService();
            deviceService.setServiceId(productEventService.getServiceId());
            deviceService.setDeviceId(productEventService.getRelationId().toString());
            return deviceService;
        }).collect(Collectors.toList());
        productEventRuleVO.setDeviceServices(deviceServiceList);

        return productEventRuleVO;
    }


    /**
     * 删除产品活动规则
     *
     * @param ruleId 规则id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProductEventRule(Long ruleId) {

        ProductEventRelation productEventRelation = productEventRelationService.getOne(Wrappers.lambdaQuery(ProductEventRelation.class).eq(ProductEventRelation::getEventRuleId, ruleId).eq(ProductEventRelation::getInherit, "0"));
        Assert.notNull(productEventRelation, "产品活动规则不存在");

        //step 0:删除 zbx触发器
        List<TriggerService.Triggers> triggersList = triggerService.triggerListGet(productEventRelation.getZbxId());
        if (CollectionUtil.isNotEmpty(triggersList)){
            triggerService.triggerDelete(productEventRelation.getZbxId());
        }

        //step 1:删除 与产品 设备的关联
        productEventRelationService.remove(Wrappers.lambdaQuery(ProductEventRelation.class).eq(ProductEventRelation::getEventRuleId, ruleId));

        //step 2:删除 关联的执行服务
        eventServiceService.remove(Wrappers.lambdaQuery(ProductEventService.class).eq(ProductEventService::getEventRuleId, ruleId));

        //step 3:删除 关联的表达式
        productEventExpressionService.remove(Wrappers.lambdaQuery(ProductEventExpression.class).eq(ProductEventExpression::getRuleId, ruleId));

        //step 4:删除 触发器
        return this.removeById(ruleId);
    }

    /**
     * 创建产品报警规则
     *
     * @param eventRule 薄
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createProductEventRule(ProductEventRuleBO eventRule) {

        // ruleId, trigger name
        Long eventRuleId = IdUtil.getSnowflakeNextId();
        eventRule.setEventRuleId(eventRuleId);

        // 触发器 Tag
        Map<String, String> tags = new ConcurrentHashMap<>(3);
        if (CollectionUtil.isNotEmpty(eventRule.getTags())) {
            tags = eventRule.getTags().stream().collect(Collectors.toMap(ProductEventRuleBO.Tag::getTag, ProductEventRuleBO.Tag::getValue, (k1, k2) -> k2));
        }
        if (!tags.containsKey(ALARM_TAG_NAME)) {
            tags.put(ALARM_TAG_NAME, "{HOST.HOST}");
        }
        if (CollectionUtil.isNotEmpty(eventRule.getDeviceServices()) && !tags.containsKey(EXECUTE_TAG_NAME)) {
            tags.put(EXECUTE_TAG_NAME, eventRuleId.toString());
        }
        // 创建 zbx 触发器
        String expression = eventRule.getExpList().stream().map(Object::toString).collect(Collectors.joining(" " + eventRule.getExpLogic() + " "));
        String triggerId = triggerService.createZbxTrigger(eventRuleId + "", expression, eventRule.getEventLevel(), tags);

        // 保存产品告警规则
        ProductEvent productEvent = productEventConvert.toEntity(eventRule);
        productEvent.setId(eventRuleId);
        baseMapper.insert(productEvent);

        // 保存 表达式，方便回显
        List<ProductEventExpression> expList = new ArrayList<>();
        eventRule.getExpList().forEach(exp -> {
            ProductEventExpression productEventExpression = productEventExpressionConvert.toEntity(exp);
            productEventExpression.setRuleId(eventRuleId);
            expList.add(productEventExpression);
        });
        productEventExpressionService.saveBatch(expList);

        //step 3: 保存执行动作 与 规则关联
        if (CollectionUtil.isNotEmpty(eventRule.getDeviceServices())) {
            List<ProductEventService> productEventServiceList = new ArrayList<>();
            eventRule.getDeviceServices().forEach(deviceService -> {
                ProductEventService productEventService = new ProductEventService();
                productEventService.setEventRuleId(eventRuleId);
                productEventService.setServiceId(deviceService.getServiceId());
                productEventService.setRelationId(eventRule.getProductId());
                productEventService.setExecuteDeviceId(0L);
                productEventServiceList.add(productEventService);
            });
            eventServiceService.saveBatch(productEventServiceList);
        }

        //step 4: 保存关联关系
        ProductEventRelation productEventRelation = new ProductEventRelation(eventRule.getEventRuleId(), eventRule.getProductId(), triggerId, eventRule.getRemark());
        return productEventRelationService.save(productEventRelation);
    }


    /**
     * 查询告警规则
     */
    @Override
    public ProductEventVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询告警规则列表
     */
    @Override
    public TableDataInfo<ProductEventVO> queryPageList(ProductEventBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductEvent> lqw = buildQueryWrapper(bo);
        Page<ProductEventVO> result = baseMapper.selectVoListPage(pageQuery.build(), lqw);
        result.getRecords().forEach(item -> {
            item.setLevelDescribe(SeverityEnum.getDescribe(item.getLevel().toString()));
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询告警规则列表
     */
    @Override
    public List<ProductEventVO> queryList(ProductEventBO bo) {
        LambdaQueryWrapper<ProductEvent> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductEvent> buildQueryWrapper(ProductEventBO bo) {
        Map<String, Object> params = bo.getParams();
        List<Long> relationIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(bo.getProductId())){
            relationIds.add(bo.getProductId());
        }
        if (ObjectUtil.isNotNull(bo.getDeviceId())){
            relationIds.add(bo.getDeviceId());
        }
        LambdaQueryWrapper<ProductEvent> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductEvent::getName, bo.getName());
        lqw.eq(bo.getLevel() != null, ProductEvent::getLevel, bo.getLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getExpLogic()), ProductEvent::getExpLogic, bo.getExpLogic());
        lqw.eq(bo.getNotify() != null, ProductEvent::getNotify, bo.getNotify());
        lqw.eq(bo.getClassify() != null, ProductEvent::getClassify, bo.getClassify());
        lqw.eq(bo.getTaskId() != null, ProductEvent::getTaskId, bo.getTaskId());
        lqw.eq(bo.getTriggerType() != null, ProductEvent::getTriggerType, bo.getTriggerType());
        lqw.apply(CollectionUtil.isNotEmpty(relationIds), " relation_id in ({0})", StringUtils.join(relationIds, ","));
        return lqw;
    }

    /**
     * 新增告警规则
     */
    @Override
    public Boolean insertByBo(ProductEventBO bo) {
        ProductEvent add = BeanUtil.toBean(bo, ProductEvent.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改告警规则
     */
    @Override
    public Boolean updateByBo(ProductEventBO bo) {
        ProductEvent update = BeanUtil.toBean(bo, ProductEvent.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductEvent entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除告警规则
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
