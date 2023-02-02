package com.pangu.iot.manager.device.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.device.domain.DeviceStatusFunctionRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionRelationBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionRelationVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 设备上下线规则与设备关系Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Mapper(componentModel = "spring")
public interface DeviceStatusFunctionRelationConvert extends CommonConvert {

    /**
     * DeviceStatusFunctionRelationBO转换为DeviceStatusFunctionRelationEntity
     *
     * @param bo DeviceStatusFunctionRelationBO对象
     * @return deviceStatusFunctionRelation
     */
    DeviceStatusFunctionRelation toEntity(DeviceStatusFunctionRelationBO bo);


    /**
     * DeviceStatusFunctionRelationVO转换为DeviceStatusFunctionRelationEntity
     *
     * @param  vo DeviceStatusFunctionRelationVO对象
     * @return deviceStatusFunctionRelation
     */
    DeviceStatusFunctionRelation toEntity(DeviceStatusFunctionRelationVO vo);

    /**
     * DeviceStatusFunctionRelation转换为DeviceStatusFunctionRelationBO
     *
     * @param  entity DeviceStatusFunctionRelation对象
     * @return deviceStatusFunctionRelationBO
     */
    DeviceStatusFunctionRelationBO toBo(DeviceStatusFunctionRelation entity);

    /**
     * DeviceStatusFunctionRelation转换为DeviceStatusFunctionRelationVO
     *
     * @param entity DeviceStatusFunctionRelation
     * @return deviceStatusFunctionRelationVO
     */
    DeviceStatusFunctionRelationVO toVo(DeviceStatusFunctionRelation entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DeviceStatusFunctionRelationBO>对象
     * @return List<DeviceStatusFunctionRelation>
     */
    List<DeviceStatusFunctionRelation> boListToEntityList(List<DeviceStatusFunctionRelationBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DeviceStatusFunctionRelationVO>
     * @return List<DeviceStatusFunctionRelation>
     */
    List<DeviceStatusFunctionRelation> voListToEntityList(List<DeviceStatusFunctionRelationVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DeviceStatusFunctionRelation>
     * @return List<DeviceStatusFunctionRelationBO>
     */
    List<DeviceStatusFunctionRelationBO> toBoList(List<DeviceStatusFunctionRelation> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DeviceStatusFunctionRelation>
     * @return List<DeviceStatusFunctionRelationVO>
     */
    List<DeviceStatusFunctionRelationVO> toVoList(List<DeviceStatusFunctionRelation> entities);

}
