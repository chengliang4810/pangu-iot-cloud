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
import com.pangu.iot.manager.product.domain.bo.ProductEventRelationBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventRelationVO;
import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.mapper.ProductEventRelationMapper;
import com.pangu.iot.manager.product.service.IProductEventRelationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 告警规则关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@RequiredArgsConstructor
@Service
public class ProductEventRelationServiceImpl extends ServiceImpl<ProductEventRelationMapper, ProductEventRelation> implements IProductEventRelationService {

    private final ProductEventRelationMapper baseMapper;

    /**
     * 查询告警规则关系
     */
    @Override
    public ProductEventRelationVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询告警规则关系列表
     */
    @Override
    public TableDataInfo<ProductEventRelationVO> queryPageList(ProductEventRelationBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductEventRelation> lqw = buildQueryWrapper(bo);
        Page<ProductEventRelationVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询告警规则关系列表
     */
    @Override
    public List<ProductEventRelationVO> queryList(ProductEventRelationBO bo) {
        LambdaQueryWrapper<ProductEventRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductEventRelation> buildQueryWrapper(ProductEventRelationBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductEventRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEventRuleId() != null, ProductEventRelation::getEventRuleId, bo.getEventRuleId());
        lqw.eq(bo.getRelationId() != null, ProductEventRelation::getRelationId, bo.getRelationId());
        lqw.eq(StringUtils.isNotBlank(bo.getZbxId()), ProductEventRelation::getZbxId, bo.getZbxId());
        lqw.eq(bo.getInherit() != null, ProductEventRelation::getInherit, bo.getInherit());
        lqw.eq(bo.getStatus() != null, ProductEventRelation::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增告警规则关系
     */
    @Override
    public Boolean insertByBo(ProductEventRelationBO bo) {
        ProductEventRelation add = BeanUtil.toBean(bo, ProductEventRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改告警规则关系
     */
    @Override
    public Boolean updateByBo(ProductEventRelationBO bo) {
        ProductEventRelation update = BeanUtil.toBean(bo, ProductEventRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductEventRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除告警规则关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
