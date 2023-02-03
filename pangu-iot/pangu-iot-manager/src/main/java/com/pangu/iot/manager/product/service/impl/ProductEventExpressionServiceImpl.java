package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.product.domain.bo.ProductEventExpressionBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventExpressionVO;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.mapper.ProductEventExpressionMapper;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 告警规则达式Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@RequiredArgsConstructor
@Service
public class ProductEventExpressionServiceImpl extends ServiceImpl<ProductEventExpressionMapper, ProductEventExpression> implements IProductEventExpressionService {

    private final ProductEventExpressionMapper baseMapper;

    /**
     * 查询告警规则达式
     */
    @Override
    public ProductEventExpressionVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询告警规则达式列表
     */
    @Override
    public TableDataInfo<ProductEventExpressionVO> queryPageList(ProductEventExpressionBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductEventExpression> lqw = buildQueryWrapper(bo);
        Page<ProductEventExpressionVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询告警规则达式列表
     */
    @Override
    public List<ProductEventExpressionVO> queryList(ProductEventExpressionBO bo) {
        LambdaQueryWrapper<ProductEventExpression> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductEventExpression> buildQueryWrapper(ProductEventExpressionBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductEventExpression> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getRuleId() != null, ProductEventExpression::getRuleId, bo.getRuleId());
        lqw.eq(StringUtils.isNotBlank(bo.getFunction()), ProductEventExpression::getFunction, bo.getFunction());
        lqw.eq(StringUtils.isNotBlank(bo.getScope()), ProductEventExpression::getScope, bo.getScope());
        lqw.eq(StringUtils.isNotBlank(bo.getCondition()), ProductEventExpression::getCondition, bo.getCondition());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), ProductEventExpression::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), ProductEventExpression::getUnit, bo.getUnit());
        lqw.eq(bo.getRelationId() != null, ProductEventExpression::getRelationId, bo.getRelationId());
        lqw.eq(bo.getProductAttributeId() != null, ProductEventExpression::getProductAttributeId, bo.getProductAttributeId());
        lqw.eq(StringUtils.isNotBlank(bo.getProductAttributeType()), ProductEventExpression::getProductAttributeType, bo.getProductAttributeType());
        lqw.eq(StringUtils.isNotBlank(bo.getPeriod()), ProductEventExpression::getPeriod, bo.getPeriod());
        lqw.eq(StringUtils.isNotBlank(bo.getAttributeValueType()), ProductEventExpression::getAttributeValueType, bo.getAttributeValueType());
        return lqw;
    }

    /**
     * 新增告警规则达式
     */
    @Override
    public Boolean insertByBo(ProductEventExpressionBO bo) {
        ProductEventExpression add = BeanUtil.toBean(bo, ProductEventExpression.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改告警规则达式
     */
    @Override
    public Boolean updateByBo(ProductEventExpressionBO bo) {
        ProductEventExpression update = BeanUtil.toBean(bo, ProductEventExpression.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductEventExpression entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除告警规则达式
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
