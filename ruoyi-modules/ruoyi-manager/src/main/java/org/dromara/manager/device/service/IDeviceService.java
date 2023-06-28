package org.dromara.manager.device.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.bo.ChildDeviceBo;
import org.dromara.manager.device.domain.bo.DeviceBo;
import org.dromara.manager.device.domain.vo.DeviceVo;

import java.util.List;

/**
 * 设备Service接口
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
public interface IDeviceService {

    /**
     * 通过驱动ID查询设备列表
     */
    List<DeviceVo> queryDeviceListByDriverId(Long driverId, Boolean enabled);

    /**
     * 查询设备
     */
    DeviceVo queryById(Long id);

    /**
     * 查询设备列表
     */
    TableDataInfo<DeviceVo> queryPageList(DeviceBo bo, PageQuery pageQuery);

    /**
     * 查询设备列表
     */
    List<DeviceVo> queryList(DeviceBo bo);

    /**
     * 新增设备
     */
    Boolean insertByBo(DeviceBo bo);

    /**
     * 修改设备
     */
    Boolean updateByBo(DeviceBo bo);

    /**
     * 查询子设备通过设备id列表
     * 查询设备通过网关id列表
     *
     * @param deviceId 设备id
     * @param enabled  启用
     * @return {@link List}<{@link DeviceVo}>
     */
    List<DeviceVo> queryChildDeviceListByDeviceId(Long deviceId, Boolean enabled);

    /**
     * 添加子设备
     *
     * @param bo 薄
     * @return int {@link Integer} 成功条数
     */
    Integer addChildDevice(ChildDeviceBo bo);

    /**
     * 数子设备id
     *
     * @param id id
     * @param enabled  启用状态
     * @return {@link Long}
     */
    Long countChildByDeviceId(Long id, Boolean enabled);

    /**
     * 根据ID删除设备信息
     *
     * @param deviceId 设备id
     * @return {@link Boolean}
     */
    Boolean deleteById(Long deviceId);

    /**
     * 根据产品id统计设备数量
     *
     * @param id id
     * @return {@link Long}
     */
    Long countDeviceByProductId(Long id);
}
