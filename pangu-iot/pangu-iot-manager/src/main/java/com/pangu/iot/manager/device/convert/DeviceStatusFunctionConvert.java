package com.pangu.iot.manager.device.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionBO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusJudgeRuleBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 设备上下线规则Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Mapper(componentModel = "spring")
public interface DeviceStatusFunctionConvert extends CommonConvert {

    /**
     * 实体
     *
     * @param bo
     * @return {@link DeviceStatusFunction}
     */
    DeviceStatusFunction toEntity(DeviceStatusJudgeRuleBO bo);

    /**
     * DeviceStatusFunctionBO转换为DeviceStatusFunctionEntity
     *
     * @param bo DeviceStatusFunctionBO对象
     * @return deviceStatusFunction
     */
    DeviceStatusFunction toEntity(DeviceStatusFunctionBO bo);


    /**
     * DeviceStatusFunctionVO转换为DeviceStatusFunctionEntity
     *
     * @param  vo DeviceStatusFunctionVO对象
     * @return deviceStatusFunction
     */
    DeviceStatusFunction toEntity(DeviceStatusFunctionVO vo);

    /**
     * DeviceStatusFunction转换为DeviceStatusFunctionBO
     *
     * @param  entity DeviceStatusFunction对象
     * @return deviceStatusFunctionBO
     */
    DeviceStatusFunctionBO toBo(DeviceStatusFunction entity);

    /**
     * DeviceStatusFunction转换为DeviceStatusFunctionVO
     *
     * @param entity DeviceStatusFunction
     * @return deviceStatusFunctionVO
     */
    DeviceStatusFunctionVO toVo(DeviceStatusFunction entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DeviceStatusFunctionBO>对象
     * @return List<DeviceStatusFunction>
     */
    List<DeviceStatusFunction> boListToEntityList(List<DeviceStatusFunctionBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DeviceStatusFunctionVO>
     * @return List<DeviceStatusFunction>
     */
    List<DeviceStatusFunction> voListToEntityList(List<DeviceStatusFunctionVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DeviceStatusFunction>
     * @return List<DeviceStatusFunctionBO>
     */
    List<DeviceStatusFunctionBO> toBoList(List<DeviceStatusFunction> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DeviceStatusFunction>
     * @return List<DeviceStatusFunctionVO>
     */
    List<DeviceStatusFunctionVO> toVoList(List<DeviceStatusFunction> entities);

}
