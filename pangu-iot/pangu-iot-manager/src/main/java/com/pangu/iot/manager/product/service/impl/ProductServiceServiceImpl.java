package com.pangu.iot.manager.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.mapper.DeviceMapper;
import com.pangu.iot.manager.product.convert.ProductServiceConvert;
import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.manager.api.domain.ProductService;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.domain.ProductServiceRelation;
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.mapper.ProductServiceMapper;
import com.pangu.iot.manager.product.service.IProductEventServiceService;
import com.pangu.iot.manager.product.service.IProductServiceParamService;
import com.pangu.iot.manager.product.service.IProductServiceRelationService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import com.pangu.manager.api.domain.Device;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    private final DeviceMapper deviceMapper;
    private final ProductServiceMapper baseMapper;
    private final IProductServiceParamService serviceParamService;
    private final ProductServiceConvert productServiceConvert;
    private final IProductServiceRelationService serviceRelationService;
    private final IProductEventServiceService productEventServiceService;

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
        Page<ProductServiceVO> result = baseMapper.selectProduceVoPageList(pageQuery.build(), lqw);
        // buildServiceParam(result.getRecords());
        // 计算产品功能是否是继承的
        buildServiceInherit(bo.getRelationId(), result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 计算产品功能是否是继承的
     *
     * @param deviceId 设备id
     * @param records  记录
     */
    private void buildServiceInherit(Long deviceId, List<ProductServiceVO> records) {
        if (ObjectUtil.isNull(deviceId) || CollectionUtil.isEmpty(records)) {
            return;
        }
        records.parallelStream().filter(productServiceVO -> !deviceId.equals(productServiceVO.getRelationId())).forEach(productServiceVO -> productServiceVO.setInherit(true));
    }

//    /**
//     * 构建服务参数
//     *
//     * @param serviceVoList
//     */
//    private void buildServiceParam(List<ProductServiceVO> serviceVoList) {
//        //查询关联的参数
//        List<Long> sids = serviceVoList.parallelStream().map(ProductServiceVO::getId).collect(Collectors.toList());
//        if (sids.isEmpty()) {
//            return;
//        }
////        List<ProductServiceParam> serviceParams = serviceParamService.list(Wrappers.<ProductServiceParam>lambdaQuery().in(ProductServiceParam::getServiceId, sids));
////        Map<Long, List<ProductServiceParam>> map = serviceParams.parallelStream().collect(Collectors.groupingBy(ProductServiceParam::getServiceId));
////        serviceVoList.forEach(productService -> {
////            if (null != map.get(productService.getId())) {
////                productService.setProductServiceParamList(map.get(productService.getId()));
////            }
////        });
//    }

    /**
     * 查询产品功能列表
     */
    @Override
    public List<ProductServiceVO> queryList(ProductServiceBO bo) {
        LambdaQueryWrapper<ProductService> lqw = buildQueryWrapper(bo);
        List<ProductServiceVO> serviceVOList = baseMapper.selectProduceVoList(lqw);
        // buildServiceParam(serviceVOList);
        // 计算产品功能是否是继承的
        buildServiceInherit(bo.getProdId(), serviceVOList);
        return serviceVOList;
    }

    private LambdaQueryWrapper<ProductService> buildQueryWrapper(ProductServiceBO bo) {
        Map<String, Object> params = bo.getParams();
        List<Long> ids = new ArrayList<>();
        if (ObjectUtil.isNotNull(bo.getProdId())){
            ids.add(bo.getProdId());
        }
        if (ObjectUtil.isNotNull(bo.getRelationId())){
            ids.add(bo.getRelationId());
        }
        LambdaQueryWrapper<ProductService> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ProductService::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), ProductService::getMark, bo.getMark());
        lqw.eq(bo.getAsync() != null, ProductService::getAsync, bo.getAsync());
        lqw.apply(CollectionUtil.isNotEmpty(ids), buildRelationWhereSql(ids), ids.toArray());
        return lqw;
    }

    private String buildRelationWhereSql(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" relation_id in(");
        for (int i = 0; i < ids.size(); i++) {
            sb.append("{");
            sb.append(i);
            sb.append("},");
        }
        return sb.substring(0, sb.length() - 1) + ")";
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
        ProductService add = productServiceConvert.toEntity(bo);
        boolean flag = baseMapper.insert(add) > 0;

//        // 保存产品功能参数
//        List<ProductServiceParam> serviceParamList = bo.getProductServiceParamList();
//        serviceParamList.forEach(param -> param.setServiceId(add.getId()));
//        serviceParamService.saveBatch(serviceParamList);

        // 保存产品与功能关系
        Long relationId = ObjectUtil.isNotNull(bo.getRelationId()) ? bo.getRelationId() : bo.getProdId();
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
//        List<ProductServiceParam> serviceParamList = bo.getProductServiceParamList();
//        serviceParamList.forEach(param -> param.setServiceId(bo.getId()));
//        serviceParamService.saveOrUpdateBatch(serviceParamList);

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
        // 该功能是否被告警规则引用
        long count = productEventServiceService.count(Wrappers.<ProductEventService>lambdaQuery().in(ProductEventService::getServiceId, ids));
        Assert.isTrue(count == 0, "该功能已被告警规则引用，无法删除");
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        // 删除产品功能参数
        serviceParamService.remove(Wrappers.<ProductServiceParam>lambdaQuery().in(ProductServiceParam::getServiceId, ids));
        // 删除产品与功能关系
        serviceRelationService.remove(Wrappers.<ProductServiceRelation>lambdaQuery().in(ProductServiceRelation::getServiceId, ids));
        return flag;
    }

    /**
     * 获取配置文件服务地图
     *
     * @param deviceIds 设备id
     * @return {@link Map}<{@link Long}, {@link Map}<{@link Long}, {@link ProductService}>>
     */
    @Override
    public Map<Long, Map<Long, ProductService>> getProfileServiceMap(Set<Long> deviceIds) {
        Map<Long, Map<Long, ProductService>> attributeVOMap = new ConcurrentHashMap<>(16);
        deviceIds.forEach(deviceId -> {
            List<ProductService> productServiceList = getProductServiceByDeviceId(deviceId);
            attributeVOMap.put(deviceId, productServiceList.stream().collect(Collectors.toMap(ProductService::getId, productService -> productService)));
        });
        return attributeVOMap;
    }


    private List<ProductService> getProductServiceByDeviceId(Long deviceId){
        Device device = deviceMapper.selectById(deviceId);
        Assert.notNull(device, "设备不存在");
        List<ProductServiceRelation> serviceRelations = serviceRelationService.list(Wrappers.<ProductServiceRelation>lambdaQuery().in(ProductServiceRelation::getRelationId, Arrays.asList(device.getProductId(), device.getId())));
        if (CollectionUtil.isEmpty(serviceRelations)){
            return Collections.emptyList();
        }
        List<Long> ids = serviceRelations.stream().map(ProductServiceRelation::getServiceId).collect(Collectors.toList());
        return baseMapper.selectList(Wrappers.lambdaQuery(ProductService.class).in(ProductService::getId, ids));
    }

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    @Override
    @Transactional
    public Boolean deleteByProductId(Long productId) {
        List<ProductServiceRelation> serviceRelations = serviceRelationService.list(Wrappers.<ProductServiceRelation>lambdaQuery().eq(ProductServiceRelation::getRelationId, productId));
        if (CollectionUtil.isEmpty(serviceRelations)){
            return true;
        }
        serviceRelationService.remove(Wrappers.<ProductServiceRelation>lambdaQuery().eq(ProductServiceRelation::getRelationId, productId));
        List<Long> ids = serviceRelations.stream().map(ProductServiceRelation::getServiceId).collect(Collectors.toList());
        return baseMapper.deleteBatchIds(ids) > 0;
    }


}
