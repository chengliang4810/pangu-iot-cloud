package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductGroup;
import com.pangu.iot.manager.product.domain.vo.ProductGroupVO;
import com.pangu.iot.manager.product.domain.bo.ProductGroupBO;

import java.util.Collection;
import java.util.List;

/**
 * 产品分组Service接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
public interface IProductGroupService {

    /**
     * 查询产品分组
     */
    ProductGroupVO queryById(Long id);


    /**
     * 查询产品分组列表
     */
    List<ProductGroupVO> queryList(ProductGroupBO bo);

    /**
     * 修改产品分组
     */
    Boolean insertByBo(ProductGroupBO bo);

    /**
     * 修改产品分组
     */
    Boolean updateByBo(ProductGroupBO bo);

    /**
     * 校验并批量删除产品分组信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
