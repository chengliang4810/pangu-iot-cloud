package com.pangu.common.core.convert;

import java.util.List;

public interface SimpleConvert<ENTITY, BO, VO> {

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
     * Entity转换为BO
     *
     * @param  entity 实体
     * @return {@link BO}
     */
    BO toBo(ENTITY entity);

    /**
     * ENTITY 转换为VO
     *
     * @param entity 实体
     * @return {@link VO}
     */
    VO toVo(ENTITY entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<BO>对象
     * @return List<ENTITY>
     */
    List<ENTITY> boListToEntityList(List<BO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<${ClassName}VO>
     * @return List<${ClassName}>
     */
    List<ENTITY> voListToEntityList(List<VO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ENTITY>
     * @return List<BO>
     */
    List<BO> toBoList(List<ENTITY> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<${ClassName}>
     * @return List<${ClassName}VO>
     */
    List<VO> toVoList(List<ENTITY> entities);

}
