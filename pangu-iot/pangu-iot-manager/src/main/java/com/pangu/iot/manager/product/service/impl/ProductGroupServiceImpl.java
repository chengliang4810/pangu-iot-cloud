package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pangu.common.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.product.domain.bo.ProductGroupBO;
import com.pangu.iot.manager.product.domain.vo.ProductGroupVO;
import com.pangu.iot.manager.product.domain.ProductGroup;
import com.pangu.iot.manager.product.mapper.ProductGroupMapper;
import com.pangu.iot.manager.product.service.IProductGroupService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 产品分组Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Service
@RequiredArgsConstructor
public class ProductGroupServiceImpl implements IProductGroupService {

    private final ProductGroupMapper baseMapper;

    /**
     * 查询产品分组
     */
    @Override
    public ProductGroupVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询产品分组列表
     */
    @Override
    public List<ProductGroupVO> queryList(ProductGroupBO bo) {
        LambdaQueryWrapper<ProductGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductGroup> buildQueryWrapper(ProductGroupBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductGroup::getName, bo.getName());
        lqw.eq(bo.getParentId() != null, ProductGroup::getParentId, bo.getParentId());
        lqw.eq(StringUtils.isNotBlank(bo.getPids()), ProductGroup::getPids, bo.getPids());
        return lqw;
    }

    /**
     * 新增产品分组
     */
    @Override
    public Boolean insertByBo(ProductGroupBO bo) {
        ProductGroup add = BeanUtil.toBean(bo, ProductGroup.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品分组
     */
    @Override
    public Boolean updateByBo(ProductGroupBO bo) {
        ProductGroup update = BeanUtil.toBean(bo, ProductGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除产品分组
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
