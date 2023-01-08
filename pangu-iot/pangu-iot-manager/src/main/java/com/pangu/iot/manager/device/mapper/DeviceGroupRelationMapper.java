package com.pangu.iot.manager.device.mapper;

import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.DeviceGroupRelation;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupRelationVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 设备与分组关系Mapper接口
 *
 * @author chengliang4810
 * @date 2023-01-07
 */
public interface DeviceGroupRelationMapper extends BaseMapperPlus<DeviceGroupRelationMapper, DeviceGroupRelation, DeviceGroupRelationVO> {

    @Select("SELECT * FROM device_group dg LEFT JOIN device_group_relation dgr ON dg.id = dgr.group_id WHERE dgr.device_id = #{deviceId}")
    List<DeviceGroup> selectDeviceGroupListByDeviceId(Long deviceId);

}
