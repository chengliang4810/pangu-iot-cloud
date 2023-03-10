package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.bo.LastDataAttributeBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import com.pangu.manager.api.domain.DriverAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 设备属性Service接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
public interface IDeviceAttributeService extends IService<DeviceAttribute> {

    /**
     * 查询设备属性
     */
    DeviceAttributeVO queryById(Long id);

    /**
     * 查询设备属性列表
     */
    TableDataInfo<DeviceAttributeVO> queryPageList(DeviceAttributeBO bo, PageQuery pageQuery);

    /**
     * 查询设备属性列表
     */
    List<DeviceAttributeVO> queryList(DeviceAttributeBO bo);

    /**
     * 修改设备属性
     */
    Boolean insertByBo(DeviceAttributeBO bo);

    /**
     * 修改设备属性
     */
    Boolean updateByBo(DeviceAttributeBO bo);

    /**
     * 校验并批量删除设备属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    Boolean deleteByProductId(Long productId);

    /**
     * 产品属性是否存在
     *
     * @param code       KEY
     * @param productId 产品id
     * @return {@link Boolean}
     */
    Boolean existsProductAttributeBy(String code, Long productId);

    /**
     * 获取某设备所有属性包含产品属性
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DeviceAttributeVO}>
     */
    List<DeviceAttributeVO> queryVOListByDeviceId(Long deviceId);

    /**
     * 查询最新数据列表
     *
     * @param bo        薄
     * @param pageQuery 页面查询
     * @return {@link TableDataInfo}<{@link DeviceAttributeVO}>
     */
    TableDataInfo<DeviceAttributeVO> queryLatestDataList(LastDataAttributeBO bo, PageQuery pageQuery);

    /**
     * 获取配置文件属性映射
     *
     * @param deviceIds 设备id
     * @return {@link Map}<{@link Long}, {@link Map}<{@link Long}, {@link DriverAttribute}>>
     */
    Map<Long, Map<Long, DeviceAttribute>> getProfileAttributeMap(Set<Long> deviceIds);

}
