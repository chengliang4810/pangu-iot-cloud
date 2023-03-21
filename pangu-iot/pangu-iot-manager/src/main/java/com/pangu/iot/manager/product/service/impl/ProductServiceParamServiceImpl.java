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
import com.pangu.iot.manager.product.domain.bo.ProductServiceParamBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceParamVO;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.mapper.ProductServiceParamMapper;
import com.pangu.iot.manager.product.service.IProductServiceParamService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 产品功能参数Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@RequiredArgsConstructor
@Service
public class ProductServiceParamServiceImpl extends ServiceImpl<ProductServiceParamMapper, ProductServiceParam> implements IProductServiceParamService {

    private final ProductServiceParamMapper baseMapper;

    /**
     * 查询产品功能参数
     */
    @Override
    public ProductServiceParamVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询产品功能参数列表
     */
    @Override
    public TableDataInfo<ProductServiceParamVO> queryPageList(ProductServiceParamBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ProductServiceParam> lqw = buildQueryWrapper(bo);
        Page<ProductServiceParamVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询产品功能参数列表
     */
    @Override
    public List<ProductServiceParamVO> queryList(ProductServiceParamBO bo) {
        LambdaQueryWrapper<ProductServiceParam> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ProductServiceParam> buildQueryWrapper(ProductServiceParamBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ProductServiceParam> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getServiceId() != null, ProductServiceParam::getServiceId, bo.getServiceId());
        lqw.eq(StringUtils.isNotBlank(bo.getKey()), ProductServiceParam::getKey, bo.getKey());
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductServiceParam::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), ProductServiceParam::getValue, bo.getValue());
        lqw.eq(bo.getDeviceId() != null, ProductServiceParam::getDeviceId, bo.getDeviceId());
        return lqw;
    }

    /**
     * 新增产品功能参数
     */
    @Override
    public Boolean insertByBo(ProductServiceParamBO bo) {
        ProductServiceParam add = BeanUtil.toBean(bo, ProductServiceParam.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品功能参数
     */
    @Override
    public Boolean updateByBo(ProductServiceParamBO bo) {
        ProductServiceParam update = BeanUtil.toBean(bo, ProductServiceParam.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ProductServiceParam entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除产品功能参数
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
