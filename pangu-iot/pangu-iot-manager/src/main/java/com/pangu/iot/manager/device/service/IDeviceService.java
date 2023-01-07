package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
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
     * 查询设备
     */
    DeviceVO queryById(Long id);

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
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
