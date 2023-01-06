package com.pangu.iot.manager.device.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 设备分组Convert接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Mapper(componentModel = "spring")
public interface DeviceGroupConvert extends CommonConvert {

    /**
     * DeviceGroupBO转换为DeviceGroupEntity
     *
     * @param bo DeviceGroupBO对象
     * @return deviceGroup
     */
    DeviceGroup toEntity(DeviceGroupBO bo);


    /**
     * DeviceGroupVO转换为DeviceGroupEntity
     *
     * @param  vo DeviceGroupVO对象
     * @return deviceGroup
     */
    DeviceGroup toEntity(DeviceGroupVO vo);

    /**
     * DeviceGroup转换为DeviceGroupBO
     *
     * @param  entity DeviceGroup对象
     * @return deviceGroupBO
     */
    DeviceGroupBO toBo(DeviceGroup entity);

    /**
     * DeviceGroup转换为DeviceGroupVO
     *
     * @param entity DeviceGroup
     * @return deviceGroupVO
     */
    DeviceGroupVO toVo(DeviceGroup entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DeviceGroupBO>对象
     * @return List<DeviceGroup>
     */
    List<DeviceGroup> boListToEntityList(List<DeviceGroupBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DeviceGroupVO>
     * @return List<DeviceGroup>
     */
    List<DeviceGroup> voListToEntityList(List<DeviceGroupVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DeviceGroup>
     * @return List<DeviceGroupBO>
     */
    List<DeviceGroupBO> toBoList(List<DeviceGroup> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DeviceGroup>
     * @return List<DeviceGroupVO>
     */
    List<DeviceGroupVO> toVoList(List<DeviceGroup> entities);

}
