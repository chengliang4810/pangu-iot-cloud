package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductEventExpression;
import com.pangu.iot.manager.product.domain.vo.ProductEventExpressionVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventExpressionBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 告警规则达式Service接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
public interface IProductEventExpressionService extends IService<ProductEventExpression> {

    /**
     * 查询告警规则达式
     */
    ProductEventExpressionVO queryById(Long id);

    /**
     * 查询告警规则达式列表
     */
    TableDataInfo<ProductEventExpressionVO> queryPageList(ProductEventExpressionBO bo, PageQuery pageQuery);

    /**
     * 查询告警规则达式列表
     */
    List<ProductEventExpressionVO> queryList(ProductEventExpressionBO bo);

    /**
     * 修改告警规则达式
     */
    Boolean insertByBo(ProductEventExpressionBO bo);

    /**
     * 修改告警规则达式
     */
    Boolean updateByBo(ProductEventExpressionBO bo);

    /**
     * 校验并批量删除告警规则达式信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
