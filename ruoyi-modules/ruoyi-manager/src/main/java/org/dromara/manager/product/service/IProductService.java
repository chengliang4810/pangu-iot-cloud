package org.dromara.manager.product.service;

import org.dromara.manager.product.domain.Product;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.domain.bo.ProductBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 产品Service接口
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
public interface IProductService {

    /**
     * 查询产品
     */
    ProductVo queryById(Long id);

    /**
     * 查询产品列表
     */
    TableDataInfo<ProductVo> queryPageList(ProductBo bo, PageQuery pageQuery);

    /**
     * 查询产品列表
     */
    List<ProductVo> queryList(ProductBo bo);

    /**
     * 新增产品
     */
    Boolean insertByBo(ProductBo bo);

    /**
     * 修改产品
     */
    Boolean updateByBo(ProductBo bo);

    /**
     * 校验并批量删除产品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
