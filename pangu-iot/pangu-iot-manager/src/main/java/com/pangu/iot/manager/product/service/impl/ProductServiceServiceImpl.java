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
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.domain.ProductService;
import com.pangu.iot.manager.product.mapper.ProductServiceMapper;
import com.pangu.iot.manager.product.service.IProductServiceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 产品功能Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@RequiredArgsConstructor
@Service
public class ProductServiceServiceImpl extends ServiceImpl<ProductServiceMapper, ProductService> implements IProductServiceService {

    private final ProductServiceMapper baseMapper;

    /**
     * 查询产品功能
     */
    @Override
    public ProductServiceVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品功能列表
     */
    @Override
    public TableDataInfo<ProductServiceVO> queryPageList(ProductServiceBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductService> lqw = buildQueryWrapper(bo);
        Page<ProductServiceVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询产品功能列表
     */
    @Override
    public List<ProductServiceVO> queryList(ProductServiceBO bo) {
        LambdaQueryWrapper<ProductService> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductService> buildQueryWrapper(ProductServiceBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductService> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductService::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), ProductService::getMark, bo.getMark());
        lqw.eq(bo.getAsync() != null, ProductService::getAsync, bo.getAsync());
        return lqw;
    }

    /**
     * 新增产品功能
     */
    @Override
    public Boolean insertByBo(ProductServiceBO bo) {
        ProductService add = BeanUtil.toBean(bo, ProductService.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品功能
     */
    @Override
    public Boolean updateByBo(ProductServiceBO bo) {
        ProductService update = BeanUtil.toBean(bo, ProductService.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductService entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除产品功能
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
