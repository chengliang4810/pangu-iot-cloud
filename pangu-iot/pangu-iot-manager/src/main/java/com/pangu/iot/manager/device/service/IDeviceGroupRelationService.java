package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupRelationBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupRelationVO;

import java.util.Collection;
import java.util.List;

/**
 * 设备与分组关系Service接口
 *
 * @author chengliang4810
 * @date 2023-01-07
 */
public interface IDeviceGroupRelationService extends IService<DeviceGroupRelation> {

    /**
     * 查询设备与分组关系
     */
    DeviceGroupRelationVO queryById(Long id);

    /**
     * 查询设备与分组关系列表
     */
    TableDataInfo<DeviceGroupRelationVO> queryPageList(DeviceGroupRelationBO bo, PageQuery pageQuery);

    /**
     * 查询设备与分组关系列表
     */
    List<DeviceGroupRelationVO> queryList(DeviceGroupRelationBO bo);

    /**
     * 修改设备与分组关系
     */
    Boolean insertByBo(DeviceGroupRelationBO bo);

    /**
     * 修改设备与分组关系
     */
    Boolean updateByBo(DeviceGroupRelationBO bo);

    /**
     * 校验并批量删除设备与分组关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
