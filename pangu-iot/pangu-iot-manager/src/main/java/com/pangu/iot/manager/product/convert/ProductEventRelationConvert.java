package com.pangu.iot.manager.product.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.domain.bo.ProductEventRelationBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventRelationVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 告警规则关系Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Mapper(componentModel = "spring")
public interface ProductEventRelationConvert extends CommonConvert {

    /**
     * ProductEventRelationBO转换为ProductEventRelationEntity
     *
     * @param bo ProductEventRelationBO对象
     * @return productEventRelation
     */
    ProductEventRelation toEntity(ProductEventRelationBO bo);


    /**
     * ProductEventRelationVO转换为ProductEventRelationEntity
     *
     * @param  vo ProductEventRelationVO对象
     * @return productEventRelation
     */
    ProductEventRelation toEntity(ProductEventRelationVO vo);

    /**
     * ProductEventRelation转换为ProductEventRelationBO
     *
     * @param  entity ProductEventRelation对象
     * @return productEventRelationBO
     */
    ProductEventRelationBO toBo(ProductEventRelation entity);

    /**
     * ProductEventRelation转换为ProductEventRelationVO
     *
     * @param entity ProductEventRelation
     * @return productEventRelationVO
     */
    ProductEventRelationVO toVo(ProductEventRelation entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductEventRelationBO>对象
     * @return List<ProductEventRelation>
     */
    List<ProductEventRelation> boListToEntityList(List<ProductEventRelationBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductEventRelationVO>
     * @return List<ProductEventRelation>
     */
    List<ProductEventRelation> voListToEntityList(List<ProductEventRelationVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductEventRelation>
     * @return List<ProductEventRelationBO>
     */
    List<ProductEventRelationBO> toBoList(List<ProductEventRelation> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductEventRelation>
     * @return List<ProductEventRelationVO>
     */
    List<ProductEventRelationVO> toVoList(List<ProductEventRelation> entities);

}
