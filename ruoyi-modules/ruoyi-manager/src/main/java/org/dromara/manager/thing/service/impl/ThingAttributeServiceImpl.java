package org.dromara.manager.thing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.data.api.RemoteTableService;
import org.dromara.manager.device.domain.Device;
import org.dromara.manager.thing.domain.ThingAttribute;
import org.dromara.manager.thing.domain.bo.ThingAttributeBo;
import org.dromara.manager.thing.domain.vo.ThingAttributeVo;
import org.dromara.manager.thing.mapper.ThingAttributeMapper;
import org.dromara.manager.device.mapper.DeviceMapper;
import org.dromara.manager.thing.service.IThingAttributeService;
import org.dromara.manager.driver.domain.bo.PointAttributeValueBo;
import org.dromara.manager.driver.service.IPointAttributeValueService;
import org.dromara.manager.product.domain.Product;
import org.dromara.manager.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 物模型属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThingAttributeServiceImpl implements IThingAttributeService {

    @DubboReference
    private RemoteTableService remoteTableService;
    private final ThingAttributeMapper baseMapper;
    private final DeviceMapper deviceMapper;
    private final ProductMapper productMapper;
    private final IPointAttributeValueService pointAttributeValueService;

    /**
     * 查询物模型属性
     *
     * @param deviceCode
     * @param identifier
     */
    @Override
    public ThingAttributeVo queryByCodeAndIdentifier(String deviceCode, String identifier) {
        Assert.notBlank(deviceCode, "设备代码不能为空");
        Assert.notBlank(identifier, "属性标识符不能为空");
        Device device = deviceMapper.selectOne(Wrappers.lambdaQuery(Device.class).eq(Device::getCode, deviceCode).last("limit 1")
            .select(Device::getId, Device::getProductId, Device::getCode, Device::getStatus));
        if (ObjectUtil.isNull(device)) {
            log.warn("设备不存在, deviceCode: {}", deviceCode);
            return null;
        }
        LambdaQueryWrapper<ThingAttribute> query = buildQueryWrapper(new ThingAttributeBo().setProductId(device.getProductId()).setDeviceId(device.getId()).setIdentifier(identifier)).last("limit 1");
        return baseMapper.selectVoOne(query);
    }

    /**
     * 查询物模型属性
     */
    @Override
    public ThingAttributeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询物模型属性列表
     */
    @Override
    public TableDataInfo<ThingAttributeVo> queryPageList(ThingAttributeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ThingAttribute> lqw = buildQueryWrapper(bo);
        Page<ThingAttributeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }



    /**
     * 查询物模型属性列表
     */
    @Override
    public List<ThingAttributeVo> queryList(ThingAttributeBo bo) {
        LambdaQueryWrapper<ThingAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }


    /**
     * 通过设备代码查询列表
     *
     * @param deviceCode 设备代码
     * @return {@link List}<{@link ThingAttributeVo}>
     */
    @Override
    public List<ThingAttributeVo> queryListByDeviceCode(String deviceCode) {
        Assert.notBlank(deviceCode, "设备代码不能为空");
        Device device = deviceMapper.selectOne(Wrappers.<Device>lambdaQuery().eq(Device::getCode, deviceCode).last("limit 1").select(Device::getId, Device::getProductId, Device::getCode, Device::getStatus));
        Assert.notNull(device, "设备不存在");
        Assert.isTrue(device.getStatus() > 0, "设备已禁用");
        return queryList(new ThingAttributeBo().setDeviceId(device.getId()).setProductId(device.getProductId()));
    }

    private LambdaQueryWrapper<ThingAttribute> buildQueryWrapper(ThingAttributeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ThingAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getId() != null, ThingAttribute::getId, bo.getId());
        lqw.in(bo.getDeviceId() != null, ThingAttribute::getDeviceId, bo.getDeviceId(), 0);
        lqw.eq(bo.getProductId() != null, ThingAttribute::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getAttributeName()), ThingAttribute::getAttributeName, bo.getAttributeName());
        lqw.eq(StringUtils.isNotBlank(bo.getIdentifier()), ThingAttribute::getIdentifier, bo.getIdentifier());
        return lqw;
    }

    /**
     * 新增物模型属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(ThingAttributeBo bo) {
        ThingAttribute add = MapstructUtils.convert(bo, ThingAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {

            // 添加点位属性配置
            savePointAttributeValue(bo);
            // 更新TdEngine表结构
            addTableField(bo);

            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 添加表字段
     *
     * @param bo 薄
     */
    private void addTableField(ThingAttributeBo bo) {
        if (ObjectUtil.isNotNull(bo.getDeviceId()) && 0 != bo.getDeviceId()) {
            // 有设备id则为物模型属性，不需要添加表字段
            return;
        }
        remoteTableService.addSuperTableField(bo.getProductId(), bo.getIdentifier(), bo.getAttributeType());
    }

    /**
     * 保存点位属性值
     *
     * @param bo 设备信息
     */
    private void savePointAttributeValue(ThingAttributeBo bo) {
        if (ObjectUtil.isNull(bo.getDeviceId()) || 0 == bo.getDeviceId()) {
            // 无设备id忽略， 一般是产品属性，则无需保存点位属性值
            return;
        }
        Map<Long, String> pointAttributeConfig = bo.getPointAttributeConfig();
        if (CollUtil.isEmpty(pointAttributeConfig)){
            // 无点位属性配置忽略
            return;
        }

        pointAttributeConfig.forEach((attributeId, value) -> {
            PointAttributeValueBo valueBo = new PointAttributeValueBo();
            valueBo.setDeviceId(bo.getDeviceId());
            valueBo.setDeviceAttributeId(bo.getId());
            valueBo.setPointAttributeId(attributeId);
            valueBo.setValue(value);
            pointAttributeValueService.insertByBo(valueBo);
        });

    }

    /**
     * 修改物模型属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(ThingAttributeBo attributeBo) {
        ThingAttributeVo attribute = this.queryById(attributeBo.getId());
        Assert.notNull(attribute, "属性不存在");
        Product product = productMapper.selectById(attribute.getProductId());
        Assert.notNull(product, "产品不存在");

        ThingAttribute update = MapstructUtils.convert(attributeBo, ThingAttribute.class);
        validEntityBeforeSave(update);

        // 标识符/数据类型发生改变
        if (!attribute.getIdentifier().equals(attributeBo.getIdentifier()) || !attribute.getAttributeType().equals(attributeBo.getAttributeType())) {
            // 删除原有的

            remoteTableService.deleteSuperTableField(product.getId(), attribute.getIdentifier());
            // 新增
            remoteTableService.addSuperTableField(product.getId(), attributeBo.getIdentifier(), attributeBo.getAttributeType());
        }

        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ThingAttribute entity){
        //TODO 做一些数据校验,如唯一约束
        String identifier = entity.getIdentifier();
        boolean exists = baseMapper.exists(Wrappers.lambdaQuery(ThingAttribute.class)
            .eq(ThingAttribute::getProductId, entity.getProductId())
            .eq(ThingAttribute::getIdentifier, identifier)
        );
        Assert.isFalse(exists, "标识符{}已被使用", identifier);
    }

    /**
     * 批量删除物模型属性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        Assert.notEmpty(ids, "id不能为空");

        ids.forEach(id -> {
            ThingAttributeVo thingAttributeVo = queryById(id);
            Assert.notNull(thingAttributeVo, "物模型属性不存在");
            remoteTableService.deleteSuperTableField(thingAttributeVo.getProductId(), thingAttributeVo.getIdentifier());
        });

        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 查询设备id 属性对应的列表
     *
     * @param bo
     * @return {@link List}<{@link ThingAttributeVo}>
     */
    @Override
    public List<ThingAttributeVo> queryListByProductIdAndDeviceId(ThingAttributeBo bo) {
        Assert.notNull(bo.getProductId(), "产品id不能为空");
        Assert.notNull(bo.getDeviceId(), "设备id不能为空");
        return baseMapper.selectVoList( Wrappers.lambdaQuery(ThingAttribute.class)
            .eq(ThingAttribute::getProductId, bo.getProductId())
            .in(ThingAttribute::getDeviceId, bo.getDeviceId(), 0)
        );
    }


    /**
     * 根据设备id仅删除物模型属性
     *
     * @param deviceId id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByDeviceId(Long deviceId) {
        Assert.notNull(deviceId, "设备id不能为空");
        return baseMapper.delete(Wrappers.lambdaQuery(ThingAttribute.class)
            .eq(ThingAttribute::getDeviceId, deviceId)
        ) > 0;
    }

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByProductId(Long productId) {
        return baseMapper.delete(Wrappers.lambdaQuery(ThingAttribute.class)
            .eq(ThingAttribute::getProductId, productId)
        ) > 0;
    }

    @Override
    public List<ThingAttributeVo> queryListByDeviceId(Long deviceId, Boolean isRealTime) {
        Assert.notNull(deviceId, "设备id不能为空");
        Device device = deviceMapper.selectById(deviceId);
        Assert.notNull(device, "设备不存在");
        List<ThingAttributeVo> thingAttributeVos = baseMapper.selectVoList(buildQueryWrapper(new ThingAttributeBo().setDeviceId(deviceId).setProductId(device.getProductId())));
        if (BooleanUtil.isTrue(isRealTime)) {
            // 关联实时数据
            realTimeData(device.getCode(), thingAttributeVos);
        }
        return thingAttributeVos;
    }

    /**
     * 实时数据
     *
     * @param records    记录
     * @param deviceCode 设备代码
     */
    private void realTimeData(String deviceCode, List<ThingAttributeVo> records) {
        if (CollUtil.isEmpty(records) || StrUtil.isBlank(deviceCode)) {
            return;
        }


        Map<String, Object> dataMap = remoteTableService.selectLastData(deviceCode);
        if (CollUtil.isEmpty(dataMap)) {
            return;
        }

        records.forEach(attribute -> {
            Object value = dataMap.get(attribute.getIdentifier());
            if (ObjUtil.isNull(value)) {
                return;
            }
            attribute.setValue(value);
        });
    }

}
