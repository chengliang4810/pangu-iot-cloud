package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.entity.dto.TrapperItemDTO;
import com.pangu.common.zabbix.service.HostService;
import com.pangu.common.zabbix.service.ItemService;
import com.pangu.common.zabbix.service.TemplateService;
import com.pangu.data.api.RemoteTdEngineService;
import com.pangu.iot.manager.device.convert.DeviceAttributeConvert;
import com.pangu.iot.manager.device.convert.DeviceConvert;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.service.*;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.iot.manager.product.service.IProductServiceService;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.SUPER_TABLE_PREFIX;
import static com.pangu.common.zabbix.constant.ZabbixConstants.ATTR_SOURCE_DEPEND;

/**
 *
 * @author chengliang
 * @date 2023/01/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAndAttributeServiceImpl implements IProductAndAttributeService {

    private final ItemService itemService;
    private final HostService hostService;
    private final DeviceConvert deviceConvert;
    private final IDeviceService deviceService;
    private final IProductService productService;
    private final TemplateService templateService;
    private final RemoteTdEngineService tdEngineService;
    private final IDeviceGroupService deviceGroupService;
    private final IProductServiceService productServiceService;
    private final DeviceAttributeConvert deviceAttributeConvert;
    private final IDeviceAttributeService deviceAttributeService;
    private final IDeviceGroupRelationService deviceGroupRelationService;
    private final IDeviceStatusFunctionService deviceStatusFunctionService;
    private final IProductEventExpressionService productEventExpressionService;
    private final IDeviceEventRuleService deviceEventRuleService;

    /**
     * 删除设备 根据 ids
     *
     * @param deviceId 设备id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteDeviceByIds(Collection<Long> deviceId) {

        // zbx 删除主机以及主机下的所有监控项
        List<Device> deviceList = deviceService.list(Wrappers.<Device>lambdaQuery().in(Device::getId, deviceId));
        List<String> zbxIds = deviceList.stream().map(Device::getZbxId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(zbxIds)) {
            hostService.hostDelete(zbxIds);
        }

        // 设备按照产品ID分组
        Map<Long, List<Device>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(Device::getProductId));
        deviceMap.forEach((productId, devices) -> {
            // 删除设备属性
            List<Long> deviceIds = devices.stream().map(Device::getId).collect(Collectors.toList());
            deviceAttributeService.remove(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getDeviceId, deviceIds));
            // 删除设备组关系
            deviceGroupRelationService.remove(Wrappers.lambdaQuery(DeviceGroupRelation.class).in(DeviceGroupRelation::getDeviceId, deviceIds));
            // 删除设备
            Integer number = deviceService.deleteWithValidByIds(deviceId, false);
            // 更新产品的设备数量
            Product product = productService.getById(productId);
            product.setDeviceCount(product.getDeviceCount() - number);
            productService.updateById(product);
            // 删除设备表
            tdEngineService.dropTable(deviceIds);
        });

        return true;
    }


    /**
     * 创建设备
     *
     * @param bo 设备信息
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertDevice(DeviceBO bo) {

        // 设备编号不存在则随机生成
        if (StrUtil.isBlank(bo.getCode())){
            bo.setCode(IdUtil.getSnowflakeNextIdStr());
        }

        // 检查设备ID唯一性
        long count = deviceService.count(Wrappers.lambdaQuery(Device.class).eq(Device::getCode, bo.getCode()));
        Assert.isLessOrEqualZero(count, "设备ID已存在");

        // 产品是否存在
        Product product = productService.getById(bo.getProductId());
        Assert.notNull(product, "产品不存在");
        // 生成设备主键
        Long id = IdUtil.getSnowflake().nextId();

        // 添加设备组关系
        List<DeviceGroupRelation> groupRelations = bo.getGroupIds().stream()
            .map(groupId -> new DeviceGroupRelation().setDeviceId(id).setDeviceGroupId(groupId))
            .collect(Collectors.toList());
        deviceGroupRelationService.saveBatch(groupRelations);

        //设备对应的 zbx主机组ID,模板ID
        String templateId = product.getZbxId();
        List<DeviceGroup> deviceGroupList = deviceGroupService.list(Wrappers.lambdaQuery(DeviceGroup.class).in(DeviceGroup::getId, bo.getGroupIds()));
        List<String> groupIds = deviceGroupList.stream().map(DeviceGroup::getZbxId).collect(Collectors.toList());

        // 创建 zbx host
        String zbxId = hostService.hostCreate(String.valueOf(id), groupIds, templateId, null);

        Device device = deviceConvert.toEntity(bo);
        device.setZbxId(zbxId);
        device.setId(id);

        // 产品的设备总数+1
        productService.update(Wrappers.lambdaUpdate(Product.class).setSql("device_count = device_count + 1").eq(Product::getId, bo.getProductId()));

        return deviceService.save(device);
    }

    /**
     * 删除属性
     *
     * @param attributeIds id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAttributeByIds(Collection<Long> attributeIds) {
        Assert.notEmpty(attributeIds, "参数异常无法删除");
        //检查是否有属性依赖
        long count = deviceAttributeService.count(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getDependencyAttrId, attributeIds));
        Assert.isLessOrEqualZero(count, "属性被依赖无法删除");

        //检查属性是否被告警规则引入
        count = productEventExpressionService.count(Wrappers.lambdaQuery(ProductEventExpression.class).in(ProductEventExpression::getProductAttributeId, attributeIds));
        Assert.isLessOrEqualZero(count, "属性被告警规则引入无法删除");

        // TODO 可以考虑直接将关联的上下线规则一并删除
        count = deviceStatusFunctionService.count(Wrappers.lambdaQuery(DeviceStatusFunction.class).in(DeviceStatusFunction::getAttributeId, attributeIds).or().in(DeviceStatusFunction::getAttributeIdRecovery, attributeIds));
        Assert.isLessOrEqualZero(count, "属性被上下线规则引入无法删除");

        // 查询属性信息, 聚合所有zbxId
        List<DeviceAttribute> attributeList = deviceAttributeService.list(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getId, attributeIds));
        List<String> zbxIds = attributeList.stream().map(DeviceAttribute::getZbxId).collect(Collectors.toList());
        Assert.isGreaterZero(zbxIds.size(), "属性信息不存在");
        // 更新超级表字段
        attributeList.forEach(attribute -> {
            tdEngineService.deleteSuperTableField(SUPER_TABLE_PREFIX +  attribute.getProductId(), attribute.getKey());
        });
        //删除zbx item
        itemService.deleteTrapperItems(zbxIds);
        // 删除属性
        return deviceAttributeService.deleteWithValidByIds(attributeIds, false);
    }

    /**
     * 更新属性
     *
     * @param attributeBo 属性
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateAttribute(DeviceAttributeBO attributeBo) {
        // 产品信息
        Product product = productService.getById(attributeBo.getProductId());
        Assert.notNull(product, "产品不存在");
        DeviceAttribute attribute = deviceAttributeService.getById(attributeBo.getId());
        Assert.notNull(attribute, "属性不存在");

        // 标识符/数据类型发生改变
        if (!attribute.getKey().equals(attributeBo.getKey()) || !attribute.getValueType().equals(attributeBo.getValueType())) {
            // 删除原有的
            tdEngineService.deleteSuperTableField(SUPER_TABLE_PREFIX +  product.getId(), attribute.getKey());
            // 新增
            tdEngineService.createSuperTableField(SUPER_TABLE_PREFIX +  product.getId(), attributeBo.getKey(), attributeBo.getValueType());
        }
        DeviceAttribute deviceAttribute = deviceAttributeConvert.toEntity(attributeBo);
        // 修改zabbix 监控项
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
        trapperItemDTO.setItemId(attribute.getZbxId());
        itemService.updateTrapperItem(trapperItemDTO);

        // 入库
        return deviceAttributeService.updateById(deviceAttribute);
    }

    /**
     * 新增属性
     *
     * @param deviceAttributeBo 属性
     * @return {@link Boolean}
     */
    @Override
    public Boolean insertAttribute(DeviceAttributeBO deviceAttributeBo) {

        boolean exists = deviceAttributeService.existsProductAttributeBy(deviceAttributeBo.getKey(), deviceAttributeBo.getProductId());
        Assert.isFalse(exists, "属性[{}]已存在", deviceAttributeBo.getKey());

        // 该属性是否为依赖属性类型
        if (StrUtil.isNotBlank(deviceAttributeBo.getSource()) && ATTR_SOURCE_DEPEND.equals(deviceAttributeBo.getSource())){

            // 参数检查
            Assert.notNull(deviceAttributeBo.getDependencyAttrId(), "依赖的属性不能为空");

            // 查询关联属性
            DeviceAttribute dependencyAttribute = deviceAttributeService.getById(deviceAttributeBo.getDependencyAttrId());
            Assert.notNull(dependencyAttribute, "依赖的属性不能为空");

            // 设置属性关联
            deviceAttributeBo.setMasterItemId(dependencyAttribute.getZbxId());
        }

        // 转换模型
        DeviceAttribute deviceAttribute = deviceAttributeConvert.toEntity(deviceAttributeBo);

        // 生成主键ID
        Long id = IdUtil.getSnowflake().nextId();
        deviceAttribute.setId(id);
        // 产品zbxId
        Product product = productService.getById(deviceAttribute.getProductId());
        Assert.notNull(product, "产品不存在");
        // TdEngine添加字段
        tdEngineService.createSuperTableField(SUPER_TABLE_PREFIX + product.getId(), deviceAttribute.getKey(), deviceAttribute.getValueType());

        // Zabbix创建监控项
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
        Map<String, String> tags = new HashMap<>();
        tags.put(IotConstants.PRODUCT_ID_TAG_NAME, deviceAttribute.getProductId().toString());
        tags.put(IotConstants.ATTRIBUTE_KEY_TAG_NAME, deviceAttribute.getKey());
        trapperItemDTO.setTags(tags);
        String zabbixId = itemService.createTrapperItem(trapperItemDTO);
        Assert.notBlank(zabbixId, "创建监控项【{}】失败", trapperItemDTO.getItemName());

        // 关联zabbix id
        deviceAttribute.setZbxId(zabbixId);

        boolean save = deviceAttributeService.save(deviceAttribute);
        if (save) {
            deviceAttributeBo.setId(deviceAttributeBo.getId());
        }
        return save;
    }

    /**
     * 删除产品
     *
     * @param ids id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProductByIds(Collection<Long> ids) {
        ids.forEach(productId -> {
            // 产品是否存在
            Product product = productService.getById(productId);

            // 查询该产品下是否存在设备
            long count = deviceService.count(Wrappers.<Device>lambdaQuery().eq(Device::getProductId, productId));
            Assert.isTrue(count == 0, "产品[{}]下存在设备，无法删除", product.getName());

            // tdengine删除表
            tdEngineService.deleteSuperTable(SUPER_TABLE_PREFIX + product.getId());

            // 删除zabbix模板
            templateService.zbxTemplateDelete(product.getZbxId());

            // 删除产品关联的属性，事件等
            deviceAttributeService.deleteByProductId(productId);

            // 删除产品关联的功能
            productServiceService.deleteByProductId(productId);

            // 删除产品关联的告警
            deviceEventRuleService.deleteByProductId(productId);

            // 删除产品关联的上下线事件
            deviceStatusFunctionService.deleteByProductId(productId);

        });
        return productService.deleteWithValidByIds(ids, false);
    }

    /**
     * 转换为dto监控项
     *
     * @param item 项
     * @return {@link TrapperItemDTO}
     */
    public static TrapperItemDTO convertToTrapperItemDTO(DeviceAttribute item) {
        if (item == null) {
            return null;
        }
        TrapperItemDTO result = new TrapperItemDTO();
        result.setHostId(item.getZbxId());
        result.setItemName(item.getId().toString());
        result.setKey(item.getKey());
        result.setSource(item.getSource());
        result.setValueType(item.getValueType());
        result.setUnits(item.getUnit());
        result.setDependencyItemZbxId(item.getMasterItemId());
        result.setValueMapId(item.getValueMapId());
        return result;
    }

}
