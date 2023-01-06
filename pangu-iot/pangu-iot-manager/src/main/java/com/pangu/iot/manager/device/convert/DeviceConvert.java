package com.pangu.iot.manager.device.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 设备Convert接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Mapper(componentModel = "spring")
public interface DeviceConvert extends CommonConvert {

    /**
     * DeviceBO转换为DeviceEntity
     *
     * @param bo DeviceBO对象
     * @return device
     */
    Device toEntity(DeviceBO bo);


    /**
     * DeviceVO转换为DeviceEntity
     *
     * @param  vo DeviceVO对象
     * @return device
     */
    Device toEntity(DeviceVO vo);

    /**
     * Device转换为DeviceBO
     *
     * @param  entity Device对象
     * @return deviceBO
     */
    DeviceBO toBo(Device entity);

    /**
     * Device转换为DeviceVO
     *
     * @param entity Device
     * @return deviceVO
     */
    DeviceVO toVo(Device entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DeviceBO>对象
     * @return List<Device>
     */
    List<Device> boListToEntityList(List<DeviceBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DeviceVO>
     * @return List<Device>
     */
    List<Device> voListToEntityList(List<DeviceVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<Device>
     * @return List<DeviceBO>
     */
    List<DeviceBO> toBoList(List<Device> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<Device>
     * @return List<DeviceVO>
     */
    List<DeviceVO> toVoList(List<Device> entities);

}
