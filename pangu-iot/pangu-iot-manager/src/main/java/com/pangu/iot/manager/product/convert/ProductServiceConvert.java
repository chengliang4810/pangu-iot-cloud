package com.pangu.iot.manager.product.convert;

import java.util.List;

import com.pangu.iot.manager.product.domain.ProductService;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 产品功能Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Mapper(componentModel = "spring")
public interface ProductServiceConvert extends CommonConvert {

    /**
     * ProductServiceBO转换为ProductServiceEntity
     *
     * @param bo ProductServiceBO对象
     * @return productService
     */
    ProductService toEntity(ProductServiceBO bo);


    /**
     * ProductServiceVO转换为ProductServiceEntity
     *
     * @param  vo ProductServiceVO对象
     * @return productService
     */
    ProductService toEntity(ProductServiceVO vo);

    /**
     * ProductService转换为ProductServiceBO
     *
     * @param  entity ProductService对象
     * @return productServiceBO
     */
    ProductServiceBO toBo(ProductService entity);

    /**
     * ProductService转换为ProductServiceVO
     *
     * @param entity ProductService
     * @return productServiceVO
     */
    ProductServiceVO toVo(ProductService entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductServiceBO>对象
     * @return List<ProductService>
     */
    List<ProductService> boListToEntityList(List<ProductServiceBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductServiceVO>
     * @return List<ProductService>
     */
    List<ProductService> voListToEntityList(List<ProductServiceVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductService>
     * @return List<ProductServiceBO>
     */
    List<ProductServiceBO> toBoList(List<ProductService> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductService>
     * @return List<ProductServiceVO>
     */
    List<ProductServiceVO> toVoList(List<ProductService> entities);

}
