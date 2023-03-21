package com.pangu.iot.manager.driver.convert;

import java.util.List;

import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.domain.vo.PointInfoVO;
import com.pangu.iot.manager.driver.domain.bo.PointInfoBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 点位属性配置信息Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Mapper(componentModel = "spring")
public interface PointInfoConvert extends CommonConvert {

    /**
     * PointInfoBO转换为PointInfoEntity
     *
     * @param bo PointInfoBO对象
     * @return pointInfo
     */
    PointInfo toEntity(PointInfoBO bo);


    /**
     * PointInfoVO转换为PointInfoEntity
     *
     * @param  vo PointInfoVO对象
     * @return pointInfo
     */
    PointInfo toEntity(PointInfoVO vo);

    /**
     * PointInfo转换为PointInfoBO
     *
     * @param  entity PointInfo对象
     * @return pointInfoBO
     */
    PointInfoBO toBo(PointInfo entity);

    /**
     * PointInfo转换为PointInfoVO
     *
     * @param entity PointInfo
     * @return pointInfoVO
     */
    PointInfoVO toVo(PointInfo entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<PointInfoBO>对象
     * @return List<PointInfo>
     */
    List<PointInfo> boListToEntityList(List<PointInfoBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<PointInfoVO>
     * @return List<PointInfo>
     */
    List<PointInfo> voListToEntityList(List<PointInfoVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<PointInfo>
     * @return List<PointInfoBO>
     */
    List<PointInfoBO> toBoList(List<PointInfo> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<PointInfo>
     * @return List<PointInfoVO>
     */
    List<PointInfoVO> toVoList(List<PointInfo> entities);

}
