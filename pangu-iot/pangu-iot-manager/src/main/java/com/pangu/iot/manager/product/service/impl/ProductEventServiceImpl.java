package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.enums.SeverityEnum;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.zabbix.service.TriggerService;
import com.pangu.iot.manager.product.convert.ProductEventConvert;
import com.pangu.iot.manager.product.convert.ProductEventExpressionConvert;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.domain.bo.ProductEventBO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import com.pangu.iot.manager.product.mapper.ProductEventMapper;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import com.pangu.iot.manager.product.service.IProductEventRelationService;
import com.pangu.iot.manager.product.service.IProductEventService;
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
    private final IProductEventRelationService productEventRelationService;
    private final ProductEventExpressionConvert productEventExpressionConvert;
    private final IProductEventExpressionService productEventExpressionService;


    /**
     * 创建产品活动规则
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
            tags.put(EXECUTE_TAG_NAME, eventRuleId + "");
        }
        // 创建 zbx 触发器
        String expression = eventRule.getExpList().stream().map(Object::toString).collect(Collectors.joining(" " + eventRule.getExpLogic() + " "));
        String triggerId = triggerService.createZbxTrigger(eventRuleId + "", expression, eventRule.getEventLevel(), tags);

        // 保存产品告警规则
        ProductEvent productEvent = productEventConvert.toEntity(eventRule);
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
        Page<ProductEventVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        // TODO 循环查询，后续单独写SQL
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
        LambdaQueryWrapper<ProductEvent> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductEvent::getName, bo.getName());
        lqw.eq(bo.getLevel() != null, ProductEvent::getLevel, bo.getLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getExpLogic()), ProductEvent::getExpLogic, bo.getExpLogic());
        lqw.eq(bo.getNotify() != null, ProductEvent::getNotify, bo.getNotify());
        lqw.eq(bo.getClassify() != null, ProductEvent::getClassify, bo.getClassify());
        lqw.eq(bo.getTaskId() != null, ProductEvent::getTaskId, bo.getTaskId());
        lqw.eq(bo.getTriggerType() != null, ProductEvent::getTriggerType, bo.getTriggerType());
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
