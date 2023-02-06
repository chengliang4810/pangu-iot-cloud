package com.pangu.iot.manager.product.convert;

import java.util.List;

import com.pangu.iot.manager.product.domain.ProductServiceRelation;
import com.pangu.iot.manager.product.domain.vo.ProductServiceRelationVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceRelationBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 产品功能关联关系Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Mapper(componentModel = "spring")
public interface ProductServiceRelationConvert extends CommonConvert {

    /**
     * ProductServiceRelationBO转换为ProductServiceRelationEntity
     *
     * @param bo ProductServiceRelationBO对象
     * @return productServiceRelation
     */
    ProductServiceRelation toEntity(ProductServiceRelationBO bo);


    /**
     * ProductServiceRelationVO转换为ProductServiceRelationEntity
     *
     * @param  vo ProductServiceRelationVO对象
     * @return productServiceRelation
     */
    ProductServiceRelation toEntity(ProductServiceRelationVO vo);

    /**
     * ProductServiceRelation转换为ProductServiceRelationBO
     *
     * @param  entity ProductServiceRelation对象
     * @return productServiceRelationBO
     */
    ProductServiceRelationBO toBo(ProductServiceRelation entity);

    /**
     * ProductServiceRelation转换为ProductServiceRelationVO
     *
     * @param entity ProductServiceRelation
     * @return productServiceRelationVO
     */
    ProductServiceRelationVO toVo(ProductServiceRelation entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductServiceRelationBO>对象
     * @return List<ProductServiceRelation>
     */
    List<ProductServiceRelation> boListToEntityList(List<ProductServiceRelationBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductServiceRelationVO>
     * @return List<ProductServiceRelation>
     */
    List<ProductServiceRelation> voListToEntityList(List<ProductServiceRelationVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductServiceRelation>
     * @return List<ProductServiceRelationBO>
     */
    List<ProductServiceRelationBO> toBoList(List<ProductServiceRelation> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductServiceRelation>
     * @return List<ProductServiceRelationVO>
     */
    List<ProductServiceRelationVO> toVoList(List<ProductServiceRelation> entities);

}
