package com.pangu.iot.manager.device.service;

import com.pangu.iot.manager.device.domain.DeviceStatusFunctionRelation;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionRelationVO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionRelationBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 设备上下线规则与设备关系Service接口
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
public interface IDeviceStatusFunctionRelationService extends IService<DeviceStatusFunctionRelation> {

    /**
     * 查询设备上下线规则与设备关系
     */
    DeviceStatusFunctionRelationVO queryById(Long id);

    /**
     * 查询设备上下线规则与设备关系列表
     */
    TableDataInfo<DeviceStatusFunctionRelationVO> queryPageList(DeviceStatusFunctionRelationBO bo, PageQuery pageQuery);

    /**
     * 查询设备上下线规则与设备关系列表
     */
    List<DeviceStatusFunctionRelationVO> queryList(DeviceStatusFunctionRelationBO bo);

    /**
     * 修改设备上下线规则与设备关系
     */
    Boolean insertByBo(DeviceStatusFunctionRelationBO bo);

    /**
     * 修改设备上下线规则与设备关系
     */
    Boolean updateByBo(DeviceStatusFunctionRelationBO bo);

    /**
     * 校验并批量删除设备上下线规则与设备关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
