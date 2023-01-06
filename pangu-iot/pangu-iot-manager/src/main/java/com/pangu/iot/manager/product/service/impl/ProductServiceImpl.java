package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.zabbix.service.TemplateService;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.domain.bo.ProductBO;
import com.pangu.iot.manager.product.domain.vo.ProductVO;
import com.pangu.iot.manager.product.mapper.ProductMapper;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.system.api.RemoteConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.pangu.common.core.constant.ConfigKeyConstants.GLOBAL_HOST_GROUP_KEY;

/**
 * 产品Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @DubboReference
    private final RemoteConfigService configService;

    private final ProductMapper baseMapper;

    private final TemplateService templateService;

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
     * 产品编码是否存在
     *
     * @param code 代码
     * @return boolean
     */
    @Override
    public Boolean existProductCode(String code){
        return baseMapper.exists(Wrappers.lambdaQuery(Product.class).eq(Product::getCode, code));
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
        //未指定产品编码，则生成
        if(StrUtil.isBlank(bo.getCode())){
            bo.setCode(IdUtil.nanoId());
        }

        // 创建zabbix模板
        Long productId = IdUtil.getSnowflake().nextId();
        String hostGroupId = configService.getConfigByKey(GLOBAL_HOST_GROUP_KEY);
        String zbxId = templateService.zbxTemplateCreate(hostGroupId, productId.toString());

        // Bean转换，设置ID与zabbix模板ID
        Product add = BeanUtil.toBean(bo, Product.class);
        add.setId(productId);
        add.setZbxId(zbxId);

        // 入库
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
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
