package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductEventRelation;
import com.pangu.iot.manager.product.domain.vo.ProductEventRelationVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRelationBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 告警规则关系Service接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
public interface IProductEventRelationService extends IService<ProductEventRelation> {

    /**
     * 查询告警规则关系
     */
    ProductEventRelationVO queryById(Long id);

    /**
     * 查询告警规则关系列表
     */
    TableDataInfo<ProductEventRelationVO> queryPageList(ProductEventRelationBO bo, PageQuery pageQuery);

    /**
     * 查询告警规则关系列表
     */
    List<ProductEventRelationVO> queryList(ProductEventRelationBO bo);

    /**
     * 修改告警规则关系
     */
    Boolean insertByBo(ProductEventRelationBO bo);

    /**
     * 修改告警规则关系
     */
    Boolean updateByBo(ProductEventRelationBO bo);

    /**
     * 校验并批量删除告警规则关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
