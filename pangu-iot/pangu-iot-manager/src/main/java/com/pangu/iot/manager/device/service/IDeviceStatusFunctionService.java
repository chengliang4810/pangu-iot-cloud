package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionVO;

import java.util.Collection;
import java.util.List;

/**
 * 设备上下线规则Service接口
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
public interface IDeviceStatusFunctionService extends IService<DeviceStatusFunction> {

    /**
     * 查询设备上下线规则
     */
    DeviceStatusFunctionVO queryById(Long id);

    /**
     * 查询设备上下线规则列表
     */
    TableDataInfo<DeviceStatusFunctionVO> queryPageList(DeviceStatusFunctionBO bo, PageQuery pageQuery);

    /**
     * 查询设备上下线规则列表
     */
    List<DeviceStatusFunctionVO> queryList(DeviceStatusFunctionBO bo);

    /**
     * 修改设备上下线规则
     */
    Boolean insertByBo(DeviceStatusFunctionBO bo);

    /**
     * 修改设备上下线规则
     */
    Boolean updateByBo(DeviceStatusFunctionBO bo);

    /**
     * 校验并批量删除设备上下线规则信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询关系id
     *
     * @param id id
     * @return {@link DeviceStatusFunctionVO}
     */
    DeviceStatusFunctionVO queryRelationId(Long id);
}
