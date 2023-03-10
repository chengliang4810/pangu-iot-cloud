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
import com.pangu.iot.manager.device.domain.*;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.service.*;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import com.pangu.iot.manager.product.service.IProductService;
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
    private final DeviceAttributeConvert deviceAttributeConvert;
    private final IDeviceAttributeService deviceAttributeService;
    private final IDeviceGroupRelationService deviceGroupRelationService;
    private final IDeviceStatusFunctionService deviceStatusFunctionService;
    private final IProductEventExpressionService productEventExpressionService;

    /**
     * ???????????? ?????? ids
     *
     * @param deviceId ??????id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteDeviceByIds(Collection<Long> deviceId) {

        // zbx ?????????????????????????????????????????????
        List<Device> deviceList = deviceService.list(Wrappers.<Device>lambdaQuery().in(Device::getId, deviceId));
        List<String> zbxIds = deviceList.stream().map(Device::getZbxId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(zbxIds)) {
            hostService.hostDelete(zbxIds);
        }

        // ??????????????????ID??????
        Map<Long, List<Device>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(Device::getProductId));
        deviceMap.forEach((productId, devices) -> {
            // ??????????????????
            List<Long> deviceIds = devices.stream().map(Device::getId).collect(Collectors.toList());
            deviceAttributeService.remove(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getDeviceId, deviceIds));
            // ?????????????????????
            deviceGroupRelationService.remove(Wrappers.lambdaQuery(DeviceGroupRelation.class).in(DeviceGroupRelation::getDeviceId, deviceIds));
            // ????????????
            Integer number = deviceService.deleteWithValidByIds(deviceId, false);
            // ???????????????????????????
            Product product = productService.getById(productId);
            product.setDeviceCount(product.getDeviceCount() - number);
            productService.updateById(product);
            // ???????????????
            tdEngineService.dropTable(deviceIds);
        });

        return true;
    }


    /**
     * ????????????
     *
     * @param bo ????????????
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertDevice(DeviceBO bo) {

        // ????????????????????????????????????
        if (StrUtil.isBlank(bo.getCode())){
            bo.setCode(IdUtil.getSnowflakeNextIdStr());
        }

        // ????????????ID?????????
        long count = deviceService.count(Wrappers.lambdaQuery(Device.class).eq(Device::getCode, bo.getCode()));
        Assert.isLessOrEqualZero(count, "??????ID?????????");

        // ??????????????????
        Product product = productService.getById(bo.getProductId());
        Assert.notNull(product, "");
        // ??????????????????
        Long id = IdUtil.getSnowflake().nextId();

        // ?????????????????????
        List<DeviceGroupRelation> groupRelations = bo.getGroupIds().stream()
            .map(groupId -> new DeviceGroupRelation().setDeviceId(id).setDeviceGroupId(groupId))
            .collect(Collectors.toList());
        deviceGroupRelationService.saveBatch(groupRelations);

        //??????????????? zbx?????????ID,??????ID
        String templateId = product.getZbxId();
        List<DeviceGroup> deviceGroupList = deviceGroupService.list(Wrappers.lambdaQuery(DeviceGroup.class).in(DeviceGroup::getId, bo.getGroupIds()));
        List<String> groupIds = deviceGroupList.stream().map(DeviceGroup::getZbxId).collect(Collectors.toList());

        // ?????? zbx host
        String zbxId = hostService.hostCreate(String.valueOf(id), groupIds, templateId, null);

        Device device = deviceConvert.toEntity(bo);
        device.setZbxId(zbxId);
        device.setId(id);

        // ?????????????????????+1
        productService.update(Wrappers.lambdaUpdate(Product.class).setSql("device_count = device_count + 1").eq(Product::getId, bo.getProductId()));

        return deviceService.save(device);
    }

    /**
     * ????????????
     *
     * @param attributeIds id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAttributeByIds(Collection<Long> attributeIds) {
        Assert.notEmpty(attributeIds, "????????????????????????");
        //???????????????????????????
        long count = deviceAttributeService.count(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getDependencyAttrId, attributeIds));
        Assert.isLessOrEqualZero(count, "???????????????????????????");

        //???????????????????????????????????????
        count = productEventExpressionService.count(Wrappers.lambdaQuery(ProductEventExpression.class).in(ProductEventExpression::getProductAttributeId, attributeIds));
        Assert.isLessOrEqualZero(count, "???????????????????????????????????????");

        // TODO ?????????????????????????????????????????????????????????
        count = deviceStatusFunctionService.count(Wrappers.lambdaQuery(DeviceStatusFunction.class).in(DeviceStatusFunction::getAttributeId, attributeIds).or().in(DeviceStatusFunction::getAttributeIdRecovery, attributeIds));
        Assert.isLessOrEqualZero(count, "??????????????????????????????????????????");

        // ??????????????????, ????????????zbxId
        List<DeviceAttribute> attributeList = deviceAttributeService.list(Wrappers.lambdaQuery(DeviceAttribute.class).in(DeviceAttribute::getId, attributeIds));
        List<String> zbxIds = attributeList.stream().map(DeviceAttribute::getZbxId).collect(Collectors.toList());
        Assert.isGreaterZero(zbxIds.size(), "?????????????????????");
        // ?????????????????????
        attributeList.forEach(attribute -> {
            tdEngineService.deleteSuperTableField(SUPER_TABLE_PREFIX +  attribute.getProductId(), attribute.getKey());
        });
        //??????zbx item
        itemService.deleteTrapperItems(zbxIds);
        // ????????????
        return deviceAttributeService.deleteWithValidByIds(attributeIds, false);
    }

    /**
     * ????????????
     *
     * @param attributeBo ??????
     * @return {@link Boolean}
     */
    @Override
    public Boolean updateAttribute(DeviceAttributeBO attributeBo) {
        // ????????????
        Product product = productService.getById(attributeBo.getProductId());
        Assert.notNull(product, "???????????????");
        DeviceAttribute attribute = deviceAttributeService.getById(attributeBo.getId());
        Assert.notNull(attribute, "???????????????");

        // ?????????/????????????????????????
        if (!attribute.getKey().equals(attributeBo.getKey()) || !attribute.getValueType().equals(attributeBo.getValueType())) {
            // ???????????????
            tdEngineService.deleteSuperTableField(SUPER_TABLE_PREFIX +  product.getId(), attribute.getKey());
            // ??????
            tdEngineService.createSuperTableField(SUPER_TABLE_PREFIX +  product.getId(), attributeBo.getKey(), attributeBo.getValueType());
        }
        DeviceAttribute deviceAttribute = deviceAttributeConvert.toEntity(attributeBo);
        // ??????zabbix ?????????
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
        trapperItemDTO.setItemId(attribute.getZbxId());
        itemService.updateTrapperItem(trapperItemDTO);

        // ??????
        return deviceAttributeService.updateById(deviceAttribute);
    }

    /**
     * ????????????
     *
     * @param deviceAttributeBo ??????
     * @return {@link Boolean}
     */
    @Override
    public Boolean insertAttribute(DeviceAttributeBO deviceAttributeBo) {

        boolean exists = deviceAttributeService.existsProductAttributeBy(deviceAttributeBo.getKey(), deviceAttributeBo.getProductId());
        Assert.isFalse(exists, "??????[{}]?????????", deviceAttributeBo.getKey());

        // ????????????????????????????????????
        if (StrUtil.isNotBlank(deviceAttributeBo.getSource()) && ATTR_SOURCE_DEPEND.equals(deviceAttributeBo.getSource())){

            // ????????????
            Assert.notNull(deviceAttributeBo.getDependencyAttrId(), "???????????????????????????");

            // ??????????????????
            DeviceAttribute dependencyAttribute = deviceAttributeService.getById(deviceAttributeBo.getDependencyAttrId());
            Assert.notNull(dependencyAttribute, "???????????????????????????");

            // ??????????????????
            deviceAttributeBo.setMasterItemId(dependencyAttribute.getZbxId());
        }

        // ????????????
        DeviceAttribute deviceAttribute = deviceAttributeConvert.toEntity(deviceAttributeBo);

        // ????????????ID
        Long id = IdUtil.getSnowflake().nextId();
        deviceAttribute.setId(id);
        // ??????zbxId
        Product product = productService.getById(deviceAttribute.getProductId());
        Assert.notNull(product, "???????????????");
        // TdEngine????????????
        tdEngineService.createSuperTableField(SUPER_TABLE_PREFIX + product.getId(), deviceAttribute.getKey(), deviceAttribute.getValueType());

        // Zabbix???????????????
        TrapperItemDTO trapperItemDTO = convertToTrapperItemDTO(deviceAttribute);
        trapperItemDTO.setHostId(product.getZbxId());
        Map<String, String> tags = new HashMap<>();
        tags.put(IotConstants.PRODUCT_ID_TAG_NAME, deviceAttribute.getProductId().toString());
        tags.put(IotConstants.ATTRIBUTE_KEY_TAG_NAME, deviceAttribute.getKey());
        trapperItemDTO.setTags(tags);
        String zabbixId = itemService.createTrapperItem(trapperItemDTO);
        Assert.notBlank(zabbixId, "??????????????????{}?????????", trapperItemDTO.getItemName());

        // ??????zabbix id
        deviceAttribute.setZbxId(zabbixId);

        boolean save = deviceAttributeService.save(deviceAttribute);
        if (save) {
            deviceAttributeBo.setId(deviceAttributeBo.getId());
        }
        return save;
    }

    /**
     * ????????????
     *
     * @param ids id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProductByIds(Collection<Long> ids) {
        ids.forEach(productId -> {
            // ??????????????????
            Product product = productService.getById(productId);

            // ????????????????????????????????????
            long count = deviceService.count(Wrappers.<Device>lambdaQuery().eq(Device::getProductId, productId));
            Assert.isTrue(count == 0, "??????[{}]??????????????????????????????", product.getName());

            // tdengine?????????
            tdEngineService.deleteSuperTable(SUPER_TABLE_PREFIX + product.getId());

            // ??????zabbix??????
            templateService.zbxTemplateDelete(product.getZbxId());

            // ???????????????????????????????????????
            deviceAttributeService.deleteByProductId(productId);
        });
        return productService.deleteWithValidByIds(ids, false);
    }

    /**
     * ?????????dto?????????
     *
     * @param item ???
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
