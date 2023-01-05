package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.vo.ProductVO;
import com.pangu.iot.manager.product.domain.bo.ProductBO;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 产品Service接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
public interface IProductService {

    /**
     * 查询产品
     */
    ProductVO queryById(Long id);

    /**
     * 查询产品列表
     */
    TableDataInfo<ProductVO> queryPageList(ProductBO bo, PageQuery pageQuery);

    /**
     * 查询产品列表
     */
    List<ProductVO> queryList(ProductBO bo);

    /**
     * 修改产品
     */
    Boolean insertByBo(ProductBO bo);

    /**
     * 修改产品
     */
    Boolean updateByBo(ProductBO bo);

    /**
     * 校验并批量删除产品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
