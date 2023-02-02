package com.pangu.iot.manager.device.mapper;

import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionVO;
import org.apache.ibatis.annotations.Select;

/**
 * 设备上下线规则Mapper接口
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
public interface DeviceStatusFunctionMapper extends BaseMapperPlus<DeviceStatusFunctionMapper, DeviceStatusFunction, DeviceStatusFunctionVO> {

    /**
     * 查询数据通过关系id
     *
     * @param id id
     * @return {@link DeviceStatusFunction}
     */
    @Select("select status_function.* from iot_device_status_function_relation relation left join iot_device_status_function status_function on relation.rule_id = status_function.id where relation.relation_id = #{id} limit 1")
    DeviceStatusFunction selectByRelationId(Long id);

}
