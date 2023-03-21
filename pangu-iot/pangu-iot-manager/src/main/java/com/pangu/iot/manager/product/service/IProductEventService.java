package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventRuleVO;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 告警规则Service接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
public interface IProductEventService extends IService<ProductEvent> {

    /**
     * 查询告警规则
     */
    ProductEventVO queryById(Long id);

    /**
     * 查询告警规则列表
     */
    TableDataInfo<ProductEventVO> queryPageList(ProductEventBO bo, PageQuery pageQuery);

    /**
     * 查询告警规则列表
     */
    List<ProductEventVO> queryList(ProductEventBO bo);

    /**
     * 修改告警规则
     */
    Boolean insertByBo(ProductEventBO bo);

    /**
     * 修改告警规则
     */
    Boolean updateByBo(ProductEventBO bo);

    /**
     * 校验并批量删除告警规则信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 创建产品活动规则
     *
     * @param bo 薄
     * @return {@link Boolean}
     */
    Boolean createProductEventRule(ProductEventRuleBO bo);

    /**
     * 删除产品活动规则
     *
     * @param ruleId 规则id
     * @return {@link Boolean}
     */
    Boolean deleteProductEventRule(Long ruleId);

    /**
     * 查询产品活动规则
     *
     * @param id id
     * @return {@link ProductEventRuleVO}
     */
    ProductEventRuleVO queryProductEventRule(Long id);

    /**
     * 更新产品事件规则id
     *
     * @param bo 薄
     * @return int
     */
    Boolean updateProductEventRuleById(ProductEventRuleBO bo);
}
