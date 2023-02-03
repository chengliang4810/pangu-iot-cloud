package com.pangu.iot.manager.product.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.domain.bo.ProductEventExpressionBO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventExpressionVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 告警规则达式Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Mapper(componentModel = "spring")
public interface ProductEventExpressionConvert extends CommonConvert {


    ProductEventExpression toEntity(ProductEventRuleBO.Expression expression);

    List<ProductEventExpression> toEntityList(List<ProductEventRuleBO.Expression> expressions);

    /**
     * ProductEventExpressionBO转换为ProductEventExpressionEntity
     *
     * @param bo ProductEventExpressionBO对象
     * @return productEventExpression
     */
    ProductEventExpression toEntity(ProductEventExpressionBO bo);


    /**
     * ProductEventExpressionVO转换为ProductEventExpressionEntity
     *
     * @param  vo ProductEventExpressionVO对象
     * @return productEventExpression
     */
    ProductEventExpression toEntity(ProductEventExpressionVO vo);

    /**
     * ProductEventExpression转换为ProductEventExpressionBO
     *
     * @param  entity ProductEventExpression对象
     * @return productEventExpressionBO
     */
    ProductEventExpressionBO toBo(ProductEventExpression entity);

    /**
     * ProductEventExpression转换为ProductEventExpressionVO
     *
     * @param entity ProductEventExpression
     * @return productEventExpressionVO
     */
    ProductEventExpressionVO toVo(ProductEventExpression entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProductEventExpressionBO>对象
     * @return List<ProductEventExpression>
     */
    List<ProductEventExpression> boListToEntityList(List<ProductEventExpressionBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProductEventExpressionVO>
     * @return List<ProductEventExpression>
     */
    List<ProductEventExpression> voListToEntityList(List<ProductEventExpressionVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ProductEventExpression>
     * @return List<ProductEventExpressionBO>
     */
    List<ProductEventExpressionBO> toBoList(List<ProductEventExpression> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ProductEventExpression>
     * @return List<ProductEventExpressionVO>
     */
    List<ProductEventExpressionVO> toVoList(List<ProductEventExpression> entities);

}
