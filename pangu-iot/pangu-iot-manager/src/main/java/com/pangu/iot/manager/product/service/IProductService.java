package com.pangu.iot.manager.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.domain.bo.ProductBO;
import com.pangu.iot.manager.product.domain.vo.ProductVO;

import java.util.Collection;
import java.util.List;

/**
 * 产品Service接口
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
public interface IProductService extends IService<Product> {

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
     * 产品是否存在
     *
     * @param code 编码
     * @return {@link Boolean}
     */
    Boolean existProductCode(String code);
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

    /**
     * 通过驱动程序id列表
     *
     * @param driverId 司机身份证
     * @return {@link List}<{@link Long}>
     */
    List<Long> listByDriverId(Long driverId);
}
