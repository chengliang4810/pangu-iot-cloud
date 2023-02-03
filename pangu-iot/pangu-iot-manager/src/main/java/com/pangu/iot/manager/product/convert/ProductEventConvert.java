package com.pangu.iot.manager.product.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.bo.ProductEventBO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * 告警规则Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Mapper(componentModel = "spring")
public interface ProductEventConvert extends CommonConvert {


    /**
     * 实体
     *
     * @param bo 薄
     * @return {@link ProductEvent}
     */
    @Mappings({
            @Mapping(target = "level", source = "eventLevel"),
            @Mapping(target = "notify", source = "eventNotify"),
            @Mapping(target = "name", source = "eventRuleName"),
    })
    ProductEvent toEntity(ProductEventRuleBO bo);

    /**
     * ProductEventBO转换为ProductEventEntity
     *
     * @param bo ProductEventBO对象
     * @return productEvent
     */
    ProductEvent toEntity(ProductEventBO bo);


    /**
     * ProductEventVO转换为ProductEventEntity
     *
     * @param  vo ProductEventVO对象
     * @return productEvent
     */
    ProductEvent toEntity(ProductEventVO vo);

    /**
     * ProductEvent转换为ProductEventBO
     *
     * @param  entity ProductEvent对象
     * @return productEventBO
     */
    ProductEventBO toBo(ProductEvent entity);

    /**
     * ProductEvent转换为ProductEventVO
     *
     * @param entity ProductEvent
     * @return productEventVO
     */
    ProductEventVO toVo(ProductEvent entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductEventBO>对象
     * @return List<ProductEvent>
     */
    List<ProductEvent> boListToEntityList(List<ProductEventBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductEventVO>
     * @return List<ProductEvent>
     */
    List<ProductEvent> voListToEntityList(List<ProductEventVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductEvent>
     * @return List<ProductEventBO>
     */
    List<ProductEventBO> toBoList(List<ProductEvent> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductEvent>
     * @return List<ProductEventVO>
     */
    List<ProductEventVO> toVoList(List<ProductEvent> entities);

}
