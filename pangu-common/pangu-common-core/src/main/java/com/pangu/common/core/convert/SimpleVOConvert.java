package com.pangu.common.core.convert;

import java.util.List;

public interface SimpleVOConvert<ENTITY, VO> {

    /**
     * BO转换为Entity
     *
     * @param bo BO对象
     * @return {@link ENTITY}
     */
    ENTITY boToEntity(VO bo);

    /**
     * VO转换为Entity
     *
     * @param  vo VO对象
     * @return {@link ENTITY}
     */
    ENTITY voToEntity(VO vo);

    /**
     * ENTITY 转换为VO
     *
     * @param entity 实体
     * @return {@link VO}
     */
    VO toVo(ENTITY entity);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<VO>
     * @return List<Entity>
     */
    List<ENTITY> voListToEntityList(List<VO> vos);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<Entity>
     * @return List<VO>
     */
    List<VO> toVoList(List<ENTITY> entities);

}
