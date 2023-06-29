package org.dromara.manager.product.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.product.domain.bo.ProductBo;
import org.dromara.manager.product.domain.vo.ProductVo;

import java.util.List;

/**
 * 产品Service接口
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
public interface IProductService {


    /**
     * 修改设备数量
     *
     * @param product id
     * @param number  数量 -1代表减少一个 1代表增加一个
     * @return {@link Boolean}
     */
    Boolean updateDeviceNumber(Long product, Integer number);

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
     * 删除产品通过id
     *
     * @param id id
     * @return {@link Boolean}
     */
    Boolean deleteById(Long id);

    /**
     * 查询父设备产品
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link ProductVo}>
     */
    List<ProductVo> queryParentDeviceProduct(Long deviceId);
}
