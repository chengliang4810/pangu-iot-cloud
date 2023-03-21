package com.pangu.iot.manager.driver.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.driver.domain.bo.PointAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.PointAttributeVO;
import com.pangu.manager.api.domain.PointAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 点位属性Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Mapper(componentModel = "spring")
public interface PointAttributeConvert extends CommonConvert {

    /**
     * PointAttributeBO转换为PointAttributeEntity
     *
     * @param bo PointAttributeBO对象
     * @return pointAttribute
     */
    PointAttribute toEntity(PointAttributeBO bo);


    /**
     * PointAttributeVO转换为PointAttributeEntity
     *
     * @param  vo PointAttributeVO对象
     * @return pointAttribute
     */
    PointAttribute toEntity(PointAttributeVO vo);

    /**
     * PointAttribute转换为PointAttributeBO
     *
     * @param  entity PointAttribute对象
     * @return pointAttributeBO
     */
    PointAttributeBO toBo(PointAttribute entity);

    /**
     * PointAttribute转换为PointAttributeVO
     *
     * @param entity PointAttribute
     * @return pointAttributeVO
     */
    @Mapping(target = "value", ignore = true)
    @Mapping(source = "value", target = "defaultValue")
    PointAttributeVO toVo(PointAttribute entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<PointAttributeBO>对象
     * @return List<PointAttribute>
     */
    List<PointAttribute> boListToEntityList(List<PointAttributeBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<PointAttributeVO>
     * @return List<PointAttribute>
     */
    List<PointAttribute> voListToEntityList(List<PointAttributeVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<PointAttribute>
     * @return List<PointAttributeBO>
     */
    List<PointAttributeBO> toBoList(List<PointAttribute> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<PointAttribute>
     * @return List<PointAttributeVO>
     */
    List<PointAttributeVO> toVoList(List<PointAttribute> entities);

}
