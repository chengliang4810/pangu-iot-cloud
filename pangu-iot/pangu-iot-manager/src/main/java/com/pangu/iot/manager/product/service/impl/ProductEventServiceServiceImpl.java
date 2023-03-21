package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.bo.ProductEventServiceBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventServiceVO;
import com.pangu.iot.manager.product.mapper.ProductEventServiceMapper;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 告警规则与功能关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-07
 */
@RequiredArgsConstructor
@Service
public class ProductEventServiceServiceImpl extends ServiceImpl<ProductEventServiceMapper, ProductEventService> implements IProductEventServiceService {

    private final ProductEventServiceMapper baseMapper;

    /**
     * 查询告警规则与功能关系
     */
    @Override
    public ProductEventServiceVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询告警规则与功能关系列表
     */
    @Override
    public TableDataInfo<ProductEventServiceVO> queryPageList(ProductEventServiceBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductEventService> lqw = buildQueryWrapper(bo);
        Page<ProductEventServiceVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询告警规则与功能关系列表
     */
    @Override
    public List<ProductEventServiceVO> queryList(ProductEventServiceBO bo) {
        LambdaQueryWrapper<ProductEventService> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductEventService> buildQueryWrapper(ProductEventServiceBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductEventService> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getEventRuleId() != null, ProductEventService::getEventRuleId, bo.getEventRuleId());
        lqw.eq(bo.getServiceId() != null, ProductEventService::getServiceId, bo.getServiceId());
        lqw.eq(bo.getRelationId() != null, ProductEventService::getRelationId, bo.getRelationId());
        lqw.eq(bo.getExecuteDeviceId() != null, ProductEventService::getExecuteDeviceId, bo.getExecuteDeviceId());
        return lqw;
    }

    /**
     * 新增告警规则与功能关系
     */
    @Override
    public Boolean insertByBo(ProductEventServiceBO bo) {
        ProductEventService add = BeanUtil.toBean(bo, ProductEventService.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改告警规则与功能关系
     */
    @Override
    public Boolean updateByBo(ProductEventServiceBO bo) {
        ProductEventService update = BeanUtil.toBean(bo, ProductEventService.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductEventService entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除告警规则与功能关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
