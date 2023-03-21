package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.product.domain.ProductServiceRelation;
import com.pangu.iot.manager.product.domain.bo.ProductServiceRelationBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceRelationVO;
import com.pangu.iot.manager.product.mapper.ProductServiceRelationMapper;
import com.pangu.iot.manager.product.service.IProductServiceRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 产品功能关联关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@RequiredArgsConstructor
@Service
public class ProductServiceRelationServiceImpl extends ServiceImpl<ProductServiceRelationMapper, ProductServiceRelation> implements IProductServiceRelationService {

    private final ProductServiceRelationMapper baseMapper;

    /**
     * 查询产品功能关联关系
     */
    @Override
    public ProductServiceRelationVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品功能关联关系列表
     */
    @Override
    public TableDataInfo<ProductServiceRelationVO> queryPageList(ProductServiceRelationBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductServiceRelation> lqw = buildQueryWrapper(bo);
        Page<ProductServiceRelationVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询产品功能关联关系列表
     */
    @Override
    public List<ProductServiceRelationVO> queryList(ProductServiceRelationBO bo) {
        LambdaQueryWrapper<ProductServiceRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductServiceRelation> buildQueryWrapper(ProductServiceRelationBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductServiceRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getServiceId() != null, ProductServiceRelation::getServiceId, bo.getServiceId());
        lqw.eq(bo.getRelationId() != null, ProductServiceRelation::getRelationId, bo.getRelationId());
        lqw.eq(bo.getInherit() != null, ProductServiceRelation::getInherit, bo.getInherit());
        return lqw;
    }

    /**
     * 新增产品功能关联关系
     */
    @Override
    public Boolean insertByBo(ProductServiceRelationBO bo) {
        ProductServiceRelation add = BeanUtil.toBean(bo, ProductServiceRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品功能关联关系
     */
    @Override
    public Boolean updateByBo(ProductServiceRelationBO bo) {
        ProductServiceRelation update = BeanUtil.toBean(bo, ProductServiceRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductServiceRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除产品功能关联关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
