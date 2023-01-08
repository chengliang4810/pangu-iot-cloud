package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.entity.dto.TrapperItemDTO;
import com.pangu.common.zabbix.service.HostService;
import com.pangu.common.zabbix.service.ItemService;
import com.pangu.common.zabbix.service.TemplateService;
import com.pangu.iot.manager.device.convert.DeviceAttributeConvert;
import com.pangu.iot.manager.device.convert.DeviceConvert;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.service.*;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private final IDeviceGroupService deviceGroupService;
    private final DeviceAttributeConvert deviceAttributeConvert;
    private final IDeviceAttributeService deviceAttributeService;
    private final IDeviceGroupRelationService deviceGroupRelationService;


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
            bo.setCode(IdUtil.nanoId());
        }

        // 检查设备ID唯一性
        long count = deviceService.count(Wrappers.lambdaQuery(Device.class).eq(Device::getCode, bo.getCode()));
        Assert.isLessOrEqualZero(count, "设备ID已存在");

        // 产品是否存在
        Product product = productService.getById(bo.getProductId());
        Assert.notNull(product, "");
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
        String zbxId = hostService.hostCreate(id.toString(), groupIds, templateId, null);

        Device device = deviceConvert.toEntity(bo);
        device.setZbxId(zbxId);
        device.setId(id);

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
//        List<Long> attrIds = new QProductAttribute().select(QProductAttribute.alias().attrId).templateId.in(productAttr.getAttrIds()).findSingleAttributeList();
//        attrIds.addAll(productAttr.getAttrIds());
//        count = new QProductEventExpression().productAttrId.in(productAttr.getAttrIds()).findCount();
//        if (count > 0) {
//            throw new ServiceException(BizExceptionEnum.PRODUCT_EVENT_HASDEPTED);
//        }
        // 查询属性信息, 聚合所有zbxId
        List<DeviceAttribute> attributeList = deviceAttributeService.list(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getId, attributeIds));
        List<String> zbxIds = attributeList.stream().map(DeviceAttribute::getZbxId).collect(Collectors.toList());
        Assert.isGreaterZero(zbxIds.size(), "属性信息不存在");
        //删除zbx item
        itemService.deleteTrapperItems(zbxIds);
        // List<String> zbxIds = new QProductAttribute().select(QProductAttribute.alias().zbxId).attrId.in(productAttr.getAttrIds()).findSingleAttributeList();
        //删除zbx item
//        if (ToolUtil.isNotEmpty(zbxIds)) {
//            List<ZbxItemInfo> itemInfos = JSONObject.parseArray(zbxItem.getItemInfo(zbxIds.toString(), null), ZbxItemInfo.class);
//            if (ToolUtil.isNotEmpty(itemInfos)) {
//                zbxItem.deleteTrapperItem(itemInfos.parallelStream().map(ZbxItemInfo::getItemid).collect(Collectors.toList()));
//            }
//        }
        //删除 属性
        //new QProductAttribute().attrId.in(productAttr.getAttrIds()).delete();

        //删除 设备 继承的属性
        //new QProductAttribute().templateId.in(productAttr.getAttrIds()).delete();

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

        DeviceAttribute deviceAttribute = deviceAttributeConvert.toEntity(attributeBo);
        // 修改zabbix 监控项
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
        trapperItemDTO.setItemId(deviceAttribute.getZbxId());
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

        // Zabbix创建监控项
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
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

            // 删除zabbix模板
            templateService.zbxTemplateDelete(product.getZbxId());

            // 删除产品关联的属性，事件等
            deviceAttributeService.deleteByProductId(productId);
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
        result.setItemName(item.getName());
        result.setKey(item.getKey());
        result.setSource(item.getSource());
        result.setValueType(item.getValueType());
        result.setUnits(item.getUnit());
        result.setDependencyItemZbxId(item.getMasterItemId());
        result.setValueMapId(item.getValueMapId());
        return result;
    }

}
