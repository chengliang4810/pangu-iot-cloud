package com.pangu.iot.manager.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备属性Mapper接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
public interface DeviceAttributeMapper extends BaseMapperPlus<DeviceAttributeMapper, DeviceAttribute, DeviceAttributeVO> {

    /**
     * 查询volist设备id
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return {@link List}<{@link DeviceAttributeVO}>
     */
    @Select("select attribute.* from iot_device_attribute attribute  where id in (select id  from iot_device_attribute where product_id = #{productId} and device_id = 0)  or device_id = #{deviceId} ")
    List<DeviceAttributeVO> queryVOListByDeviceId(@Param("productId") Long productId, @Param("deviceId") Long deviceId);

    /**
     * 查询volist设备id
     *
     * @param productId 产品id
     * @param deviceId  设备id
     * @return {@link List}<{@link DeviceAttributeVO}>
     */
    @Select("select attribute.* from iot_device_attribute attribute  where id in (select id  from iot_device_attribute where product_id = #{productId} and device_id = 0)  or device_id = #{deviceId} ")
    Page<DeviceAttributeVO> queryVOListByDeviceId(IPage page, @Param("productId") Long productId, @Param("deviceId") Long deviceId);
}
