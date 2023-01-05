package com.pangu.iot.manager.product.convert;

import java.util.List;

import com.pangu.iot.manager.product.domain.ProductGroup;
import com.pangu.iot.manager.product.domain.vo.ProductGroupVO;
import com.pangu.iot.manager.product.domain.bo.ProductGroupBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 产品分组Convert接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Mapper(componentModel = "spring")
public interface ProductGroupConvert extends CommonConvert {

    /**
     * ProductGroupBO转换为ProductGroupEntity
     *
     * @param bo ProductGroupBO对象
     * @return productGroup
     */
    ProductGroup toEntity(ProductGroupBO bo);


    /**
     * ProductGroupVO转换为ProductGroupEntity
     *
     * @param  vo ProductGroupVO对象
     * @return productGroup
     */
    ProductGroup toEntity(ProductGroupVO vo);

    /**
     * ProductGroup转换为ProductGroupBO
     *
     * @param  entity ProductGroup对象
     * @return productGroupBO
     */
    ProductGroupBO toBo(ProductGroup entity);

    /**
     * ProductGroup转换为ProductGroupVO
     *
     * @param entity ProductGroup
     * @return productGroupVO
     */
    ProductGroupVO toVo(ProductGroup entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductGroupBO>对象
     * @return List<ProductGroup>
     */
    List<ProductGroup> boListToEntityList(List<ProductGroupBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductGroupVO>
     * @return List<ProductGroup>
     */
    List<ProductGroup> voListToEntityList(List<ProductGroupVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductGroup>
     * @return List<ProductGroupBO>
     */
    List<ProductGroupBO> toBoList(List<ProductGroup> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductGroup>
     * @return List<ProductGroupVO>
     */
    List<ProductGroupVO> toVoList(List<ProductGroup> entities);

}
