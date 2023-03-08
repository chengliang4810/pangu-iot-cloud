package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.bo.DeviceGatewayBindBo;
import com.pangu.manager.api.domain.Device;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusBO;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteBO;
import com.pangu.iot.manager.device.domain.vo.DeviceDetailVO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;

import java.util.Collection;
import java.util.List;

/**
 * 设备Service接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
public interface IDeviceService extends IService<Device> {

    /**
     * 获取设备ID使用Code
     */
    Long queryDeviceIdByCode(String code);

    /**
     * 查询设备
     */
    DeviceDetailVO queryById(Long id);

    /**
     * 查询设备列表
     */
    TableDataInfo<DeviceListVO> queryPageList(DeviceBO bo, PageQuery pageQuery);

    /**
     * 查询设备列表
     */
    List<DeviceVO> queryList(DeviceBO bo);

    /**
     * 修改设备
     */
    Boolean insertByBo(DeviceBO bo);

    /**
     * 修改设备
     */
    Boolean updateByBo(DeviceBO bo);

    /**
     * 校验并批量删除设备信息
     */
    Integer deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 更新设备状态
     *
     * @param deviceStatusBO 设备状态
     * @return {@link Boolean}
     */
    Boolean updateDeviceStatus(DeviceStatusBO deviceStatusBO);

    /**
     * 执行服务
     * 执行功能
     *
     * @param deviceId      设备id
     * @param serviceId     服务id
     * @param serviceParams 服务参数
     * @param executeType   执行类型 0 手动 1 自动
     */
    void executeService(Long deviceId, Long serviceId, List<ServiceExecuteBO.ServiceParam> serviceParams, Integer executeType);

    /**
     * 执行服务
     * 执行功能
     *
     * @param deviceId    设备id
     * @param eventId     标识符
     * @param executeType 执行类型
     */
    void executeService(Long deviceId, Long eventId, Integer executeType);

    /**
     * 网关设备绑定子设备
     *
     * @param deviceGatewayBindBo 网关设备绑定博
     * @return {@link Boolean}
     */
    Boolean bindGatewayDevice(DeviceGatewayBindBo deviceGatewayBindBo);

    /**
     * 查询网关设备绑定设备id
     *
     * @param id id
     * @return {@link List}<{@link Long}>
     */
    List<Long> queryGatewayDeviceBindIds(Long id);
}
