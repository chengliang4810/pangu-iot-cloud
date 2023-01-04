package com.pangu.iot.manager.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.domain.Product;
import com.pangu.iot.manager.domain.bo.ProductBO;
import com.pangu.iot.manager.domain.vo.ProductVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 产品Convert接口
 *
 * @author chengliang4810
 * @date 2022-12-30
 */
@Mapper(componentModel = "spring")
public interface ProductConvert extends CommonConvert {

    /**
     * ProductBO转换为ProductEntity
     *
     * @param bo ProductBO对象
     * @return product
     */
    Product toEntity(ProductBO bo);


    /**
     * ProductVO转换为ProductEntity
     *
     * @param  vo ProductVO对象
     * @return product
     */
    Product toEntity(ProductVO vo);

    /**
     * Product转换为ProductBO
     *
     * @param  entity Product对象
     * @return productBO
     */
    ProductBO toBo(Product entity);

    /**
     * Product转换为ProductVO
     *
     * @param entity Product
     * @return productVO
     */
    ProductVO toVo(Product entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductBO>对象
     * @return List<Product>
     */
    List<Product> boListToEntityList(List<ProductBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductVO>
     * @return List<Product>
     */
    List<Product> voListToEntityList(List<ProductVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<Product>
     * @return List<ProductBO>
     */
    List<ProductBO> toBoList(List<Product> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<Product>
     * @return List<ProductVO>
     */
    List<ProductVO> toVoList(List<Product> entities);

}
