package com.pangu.iot.manager.driver.convert;

import java.util.List;

import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.vo.DriverInfoVO;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 驱动属性配置信息Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Mapper(componentModel = "spring")
public interface DriverInfoConvert extends CommonConvert {

    /**
     * DriverInfoBO转换为DriverInfoEntity
     *
     * @param bo DriverInfoBO对象
     * @return driverInfo
     */
    DriverInfo toEntity(DriverInfoBO bo);


    /**
     * DriverInfoVO转换为DriverInfoEntity
     *
     * @param  vo DriverInfoVO对象
     * @return driverInfo
     */
    DriverInfo toEntity(DriverInfoVO vo);

    /**
     * DriverInfo转换为DriverInfoBO
     *
     * @param  entity DriverInfo对象
     * @return driverInfoBO
     */
    DriverInfoBO toBo(DriverInfo entity);

    /**
     * DriverInfo转换为DriverInfoVO
     *
     * @param entity DriverInfo
     * @return driverInfoVO
     */
    DriverInfoVO toVo(DriverInfo entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DriverInfoBO>对象
     * @return List<DriverInfo>
     */
    List<DriverInfo> boListToEntityList(List<DriverInfoBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DriverInfoVO>
     * @return List<DriverInfo>
     */
    List<DriverInfo> voListToEntityList(List<DriverInfoVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DriverInfo>
     * @return List<DriverInfoBO>
     */
    List<DriverInfoBO> toBoList(List<DriverInfo> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DriverInfo>
     * @return List<DriverInfoVO>
     */
    List<DriverInfoVO> toVoList(List<DriverInfo> entities);

}
