package com.pangu.iot.manager.driver.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.driver.domain.bo.DriverAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.DriverAttributeVO;
import com.pangu.manager.api.domain.DriverAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 驱动属性Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Mapper(componentModel = "spring")
public interface DriverAttributeConvert extends CommonConvert {

    /**
     * DriverAttributeBO转换为DriverAttributeEntity
     *
     * @param bo DriverAttributeBO对象
     * @return driverAttribute
     */
    DriverAttribute toEntity(DriverAttributeBO bo);


    /**
     * DriverAttributeVO转换为DriverAttributeEntity
     *
     * @param  vo DriverAttributeVO对象
     * @return driverAttribute
     */
    DriverAttribute toEntity(DriverAttributeVO vo);

    /**
     * DriverAttribute转换为DriverAttributeBO
     *
     * @param  entity DriverAttribute对象
     * @return driverAttributeBO
     */
    DriverAttributeBO toBo(DriverAttribute entity);

    /**
     * DriverAttribute转换为DriverAttributeVO
     *
     * @param entity DriverAttribute
     * @return driverAttributeVO
     */
    @Mapping(target = "value", ignore = true)
    @Mapping(source = "value", target = "defaultValue")
    DriverAttributeVO toVo(DriverAttribute entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DriverAttributeBO>对象
     * @return List<DriverAttribute>
     */
    List<DriverAttribute> boListToEntityList(List<DriverAttributeBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DriverAttributeVO>
     * @return List<DriverAttribute>
     */
    List<DriverAttribute> voListToEntityList(List<DriverAttributeVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DriverAttribute>
     * @return List<DriverAttributeBO>
     */
    List<DriverAttributeBO> toBoList(List<DriverAttribute> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DriverAttribute>
     * @return List<DriverAttributeVO>
     */
    List<DriverAttributeVO> toVoList(List<DriverAttribute> entities);

}
