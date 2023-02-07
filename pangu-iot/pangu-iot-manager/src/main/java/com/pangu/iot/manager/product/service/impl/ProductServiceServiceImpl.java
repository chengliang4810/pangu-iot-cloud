package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.product.domain.ProductService;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.domain.ProductServiceRelation;
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.mapper.ProductServiceMapper;
import com.pangu.iot.manager.product.service.IProductServiceParamService;
import com.pangu.iot.manager.product.service.IProductServiceRelationService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final IProductServiceParamService serviceParamService;
    private final IProductServiceRelationService serviceRelationService;

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
        buildServiceParam(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 构建服务参数
     *
     * @param serviceVoList
     */
    private void buildServiceParam(List<ProductServiceVO> serviceVoList) {
        //查询关联的参数
        List<Long> sids = serviceVoList.parallelStream().map(ProductServiceVO::getId).collect(Collectors.toList());
        if (sids.isEmpty()) {
            return;
        }
        List<ProductServiceParam> serviceParams = serviceParamService.list(Wrappers.<ProductServiceParam>lambdaQuery().in(ProductServiceParam::getServiceId, sids));
        Map<Long, List<ProductServiceParam>> map = serviceParams.parallelStream().collect(Collectors.groupingBy(ProductServiceParam::getServiceId));
        System.out.println(map);
        serviceVoList.forEach(productService -> {
            if (null != map.get(productService.getId())) {
                productService.setProductServiceParamList(map.get(productService.getId()));
            }
        });
    }

    /**
     * 查询产品功能列表
     */
    @Override
    public List<ProductServiceVO> queryList(ProductServiceBO bo) {
        LambdaQueryWrapper<ProductService> lqw = buildQueryWrapper(bo);
        List<ProductServiceVO> serviceVOList = baseMapper.selectVoList(lqw);
        buildServiceParam(serviceVOList);
        return serviceVOList;
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
    @Transactional
    public Boolean insertByBo(ProductServiceBO bo) {

        // 功能名称是否存在
        boolean exist = baseMapper.selectCount(Wrappers.<ProductService>lambdaQuery().eq(ProductService::getName, bo.getName())) > 0;
        Assert.isFalse(exist, "功能已存在");

        // 数据入库
        ProductService add = BeanUtil.toBean(bo, ProductService.class);
        boolean flag = baseMapper.insert(add) > 0;

        // 保存产品功能参数
        List<ProductServiceParam> serviceParamList = bo.getProductServiceParamList();
        serviceParamList.forEach(param -> param.setServiceId(add.getId()));
        serviceParamService.saveBatch(serviceParamList);

        // 保存产品与功能关系
        Long relationId = bo.getRelationId();
        serviceRelationService.save(new ProductServiceRelation(add.getId(), relationId, false));

        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改产品功能
     */
    @Override
    @Transactional
    public Boolean updateByBo(ProductServiceBO bo) {

        // 根据ID获取产品功能信息
        ProductService old = baseMapper.selectById(bo.getId());
        Assert.notNull(old, "产品功能不存在");

        // 校验名称是否重复
        boolean exist = baseMapper.selectCount(Wrappers.<ProductService>lambdaQuery().eq(ProductService::getName, bo.getName()).ne(ProductService::getId, bo.getId())) > 0;
        Assert.isFalse(exist, "功能已存在");

        // 更新产品功能参数
        List<ProductServiceParam> serviceParamList = bo.getProductServiceParamList();
        serviceParamList.forEach(param -> param.setServiceId(bo.getId()));
        serviceParamService.saveOrUpdateBatch(serviceParamList);

        // 删除旧产品与功能关系，新增新的产品与功能关系
        Long relationId = bo.getRelationId();
        serviceRelationService.remove(Wrappers.<ProductServiceRelation>lambdaQuery().eq(ProductServiceRelation::getServiceId, bo.getId()));
        serviceRelationService.save(new ProductServiceRelation(bo.getId(), relationId, false));

        // 数据入库
        ProductService update = BeanUtil.toBean(bo, ProductService.class);
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
    @Transactional
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        // 删除产品功能参数
        serviceParamService.remove(Wrappers.<ProductServiceParam>lambdaQuery().in(ProductServiceParam::getServiceId, ids));
        // 删除产品与功能关系
        serviceRelationService.remove(Wrappers.<ProductServiceRelation>lambdaQuery().in(ProductServiceRelation::getServiceId, ids));
        return flag;
    }
}
