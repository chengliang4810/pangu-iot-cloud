package com.pangu.iot.manager.product.convert;

import java.util.List;

import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.domain.vo.ProductServiceParamVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceParamBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 产品功能参数Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Mapper(componentModel = "spring")
public interface ProductServiceParamConvert extends CommonConvert {

    /**
     * ProductServiceParamBO转换为ProductServiceParamEntity
     *
     * @param bo ProductServiceParamBO对象
     * @return productServiceParam
     */
    ProductServiceParam toEntity(ProductServiceParamBO bo);


    /**
     * ProductServiceParamVO转换为ProductServiceParamEntity
     *
     * @param  vo ProductServiceParamVO对象
     * @return productServiceParam
     */
    ProductServiceParam toEntity(ProductServiceParamVO vo);

    /**
     * ProductServiceParam转换为ProductServiceParamBO
     *
     * @param  entity ProductServiceParam对象
     * @return productServiceParamBO
     */
    ProductServiceParamBO toBo(ProductServiceParam entity);

    /**
     * ProductServiceParam转换为ProductServiceParamVO
     *
     * @param entity ProductServiceParam
     * @return productServiceParamVO
     */
    ProductServiceParamVO toVo(ProductServiceParam entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductServiceParamBO>对象
     * @return List<ProductServiceParam>
     */
    List<ProductServiceParam> boListToEntityList(List<ProductServiceParamBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductServiceParamVO>
     * @return List<ProductServiceParam>
     */
    List<ProductServiceParam> voListToEntityList(List<ProductServiceParamVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductServiceParam>
     * @return List<ProductServiceParamBO>
     */
    List<ProductServiceParamBO> toBoList(List<ProductServiceParam> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductServiceParam>
     * @return List<ProductServiceParamVO>
     */
    List<ProductServiceParamVO> toVoList(List<ProductServiceParam> entities);

}
