package org.dromara.manager.product.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.product.domain.Product;
import org.dromara.manager.product.domain.bo.ProductBo;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.mapper.ProductMapper;
import org.dromara.manager.product.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 产品Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    private final ProductMapper baseMapper;

    /**
     * 更新设备数量
     *
     * @param productId 产品Id
     * @param number 增减的数量
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateDeviceNumber(Long productId, Integer number) {
        ProductVo productVo = queryById(productId);
        Assert.notNull(productVo, "产品不存在");
        Product product = new Product().setDeviceCount(productVo.getDeviceCount() + number);
        return baseMapper.update(product, Wrappers.<Product>lambdaUpdate().eq(Product::getId, productId)) > 0;
    }

    /**
     * 查询产品
     */
    @Override
    public ProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品列表
     */
    @Override
    public TableDataInfo<ProductVo> queryPageList(ProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        Page<ProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询产品列表
     */
    @Override
    public List<ProductVo> queryList(ProductBo bo) {
        LambdaQueryWrapper<Product> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Product> buildQueryWrapper(ProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGroupId() != null, Product::getGroupId, bo.getGroupId());
        lqw.eq(bo.getDriverId() != null, Product::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Product::getName, bo.getName());
        lqw.eq(bo.getType() != null, Product::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), Product::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getManufacturer()), Product::getManufacturer, bo.getManufacturer());
        lqw.eq(StringUtils.isNotBlank(bo.getModel()), Product::getModel, bo.getModel());
        return lqw;
    }

    /**
     * 新增产品
     */
    @Override
    public Boolean insertByBo(ProductBo bo) {
        Product add = MapstructUtils.convert(bo, Product.class);
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
    public Boolean updateByBo(ProductBo bo) {
        Product update = MapstructUtils.convert(bo, Product.class);
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
     * 删除产品通过id
     *
     * @param id id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

}
