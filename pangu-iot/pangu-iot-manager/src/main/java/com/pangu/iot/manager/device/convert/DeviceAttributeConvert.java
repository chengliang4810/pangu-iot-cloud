package com.pangu.iot.manager.device.convert;

import java.util.List;

import com.pangu.iot.manager.device.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 设备属性Convert接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Mapper(componentModel = "spring")
public interface DeviceAttributeConvert extends CommonConvert {

    /**
     * DeviceAttributeBO转换为DeviceAttributeEntity
     *
     * @param bo DeviceAttributeBO对象
     * @return deviceAttribute
     */
    DeviceAttribute toEntity(DeviceAttributeBO bo);


    /**
     * DeviceAttributeVO转换为DeviceAttributeEntity
     *
     * @param  vo DeviceAttributeVO对象
     * @return deviceAttribute
     */
    DeviceAttribute toEntity(DeviceAttributeVO vo);

    /**
     * DeviceAttribute转换为DeviceAttributeBO
     *
     * @param  entity DeviceAttribute对象
     * @return deviceAttributeBO
     */
    DeviceAttributeBO toBo(DeviceAttribute entity);

    /**
     * DeviceAttribute转换为DeviceAttributeVO
     *
     * @param entity DeviceAttribute
     * @return deviceAttributeVO
     */
    DeviceAttributeVO toVo(DeviceAttribute entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DeviceAttributeBO>对象
     * @return List<DeviceAttribute>
     */
    List<DeviceAttribute> boListToEntityList(List<DeviceAttributeBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DeviceAttributeVO>
     * @return List<DeviceAttribute>
     */
    List<DeviceAttribute> voListToEntityList(List<DeviceAttributeVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DeviceAttribute>
     * @return List<DeviceAttributeBO>
     */
    List<DeviceAttributeBO> toBoList(List<DeviceAttribute> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DeviceAttribute>
     * @return List<DeviceAttributeVO>
     */
    List<DeviceAttributeVO> toVoList(List<DeviceAttribute> entities);

}
