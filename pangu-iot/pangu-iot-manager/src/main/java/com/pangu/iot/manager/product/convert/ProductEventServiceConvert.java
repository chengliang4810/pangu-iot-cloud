package com.pangu.iot.manager.product.convert;

import java.util.List;

import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.vo.ProductEventServiceVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventServiceBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 告警规则与功能关系Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-07
 */
@Mapper(componentModel = "spring")
public interface ProductEventServiceConvert extends CommonConvert {

    /**
     * ProductEventServiceBO转换为ProductEventServiceEntity
     *
     * @param bo ProductEventServiceBO对象
     * @return productEventService
     */
    ProductEventService toEntity(ProductEventServiceBO bo);


    /**
     * ProductEventServiceVO转换为ProductEventServiceEntity
     *
     * @param  vo ProductEventServiceVO对象
     * @return productEventService
     */
    ProductEventService toEntity(ProductEventServiceVO vo);

    /**
     * ProductEventService转换为ProductEventServiceBO
     *
     * @param  entity ProductEventService对象
     * @return productEventServiceBO
     */
    ProductEventServiceBO toBo(ProductEventService entity);

    /**
     * ProductEventService转换为ProductEventServiceVO
     *
     * @param entity ProductEventService
     * @return productEventServiceVO
     */
    ProductEventServiceVO toVo(ProductEventService entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductEventServiceBO>对象
     * @return List<ProductEventService>
     */
    List<ProductEventService> boListToEntityList(List<ProductEventServiceBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductEventServiceVO>
     * @return List<ProductEventService>
     */
    List<ProductEventService> voListToEntityList(List<ProductEventServiceVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductEventService>
     * @return List<ProductEventServiceBO>
     */
    List<ProductEventServiceBO> toBoList(List<ProductEventService> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductEventService>
     * @return List<ProductEventServiceVO>
     */
    List<ProductEventServiceVO> toVoList(List<ProductEventService> entities);

}
