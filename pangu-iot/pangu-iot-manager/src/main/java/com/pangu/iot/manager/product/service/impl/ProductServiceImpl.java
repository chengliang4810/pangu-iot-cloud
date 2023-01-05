package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.iot.manager.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.product.domain.bo.ProductBO;
import com.pangu.iot.manager.product.domain.vo.ProductVO;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.mapper.ProductMapper;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 产品Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductMapper baseMapper;

    /**
     * 查询产品
     */
    @Override
    public ProductVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品列表
     */
    @Override
    public TableDataInfo<ProductVO> queryPageList(ProductBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        Page<ProductVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询产品列表
     */
    @Override
    public List<ProductVO> queryList(ProductBO bo) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGroupId() != null, Product::getGroupId, bo.getGroupId());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Product::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Product::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), Product::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), Product::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getManufacturer()), Product::getManufacturer, bo.getManufacturer());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), Product::getModel, bo.getModel());
        return lqw;
    }

    /**
     * 新增产品
     */
    @Override
    public Boolean insertByBo(ProductBO bo) {
        Product add = BeanUtil.toBean(bo, Product.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品
     */
    @Override
    public Boolean updateByBo(ProductBO bo) {
        Product update = BeanUtil.toBean(bo, Product.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Product entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除产品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
