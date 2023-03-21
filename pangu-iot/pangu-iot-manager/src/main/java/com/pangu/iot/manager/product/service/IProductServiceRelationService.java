package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductServiceRelation;
import com.pangu.iot.manager.product.domain.vo.ProductServiceRelationVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceRelationBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 产品功能关联关系Service接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
public interface IProductServiceRelationService extends IService<ProductServiceRelation> {

    /**
     * 查询产品功能关联关系
     */
    ProductServiceRelationVO queryById(Long id);

    /**
     * 查询产品功能关联关系列表
     */
    TableDataInfo<ProductServiceRelationVO> queryPageList(ProductServiceRelationBO bo, PageQuery pageQuery);

    /**
     * 查询产品功能关联关系列表
     */
    List<ProductServiceRelationVO> queryList(ProductServiceRelationBO bo);

    /**
     * 修改产品功能关联关系
     */
    Boolean insertByBo(ProductServiceRelationBO bo);

    /**
     * 修改产品功能关联关系
     */
    Boolean updateByBo(ProductServiceRelationBO bo);

    /**
     * 校验并批量删除产品功能关联关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
