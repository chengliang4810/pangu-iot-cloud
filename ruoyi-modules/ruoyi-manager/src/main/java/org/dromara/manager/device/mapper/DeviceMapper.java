package org.dromara.manager.device.mapper;

import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.device.domain.Device;
import org.dromara.manager.device.domain.vo.DeviceVo;

import java.util.List;

/**
 * 设备Mapper接口
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
public interface DeviceMapper extends BaseMapperPlus<Device, DeviceVo> {


    /**
     * 通过驱动Id查询设备列表， 驱动ID在产品表中关联查询
     */
    List<DeviceVo> selectDeviceListByDriverId(Long driverId, Boolean enabled);

}
