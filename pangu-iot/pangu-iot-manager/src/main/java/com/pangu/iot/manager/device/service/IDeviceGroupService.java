package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupVO;

import java.util.Collection;
import java.util.List;

/**
 * 设备分组Service接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
public interface IDeviceGroupService extends IService<DeviceGroup> {

    /**
     * 查询设备分组
     */
    DeviceGroupVO queryById(Long id);

    /**
     * 查询设备分组列表
     */
    TableDataInfo<DeviceGroupVO> queryPageList(DeviceGroupBO bo, PageQuery pageQuery);

    /**
     * 查询设备分组列表
     */
    List<DeviceGroupVO> queryList(DeviceGroupBO bo);

    /**
     * 新增设备分组
     */
    Boolean insertByBo(DeviceGroupBO bo);

    /**
     * 修改设备分组
     */
    Boolean updateByBo(DeviceGroupBO bo);

    /**
     * 校验并批量删除设备分组信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
