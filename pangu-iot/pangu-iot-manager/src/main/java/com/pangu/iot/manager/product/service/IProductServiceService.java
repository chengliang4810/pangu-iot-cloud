package com.pangu.iot.manager.product.service;

import com.pangu.manager.api.domain.ProductService;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 产品功能Service接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
public interface IProductServiceService extends IService<ProductService> {

    /**
     * 查询产品功能
     */
    ProductServiceVO queryById(Long id);

    /**
     * 查询产品功能列表
     */
    TableDataInfo<ProductServiceVO> queryPageList(ProductServiceBO bo, PageQuery pageQuery);

    /**
     * 查询产品功能列表
     */
    List<ProductServiceVO> queryList(ProductServiceBO bo);

    /**
     * 修改产品功能
     */
    Boolean insertByBo(ProductServiceBO bo);

    /**
     * 修改产品功能
     */
    Boolean updateByBo(ProductServiceBO bo);

    /**
     * 校验并批量删除产品功能信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取配置文件服务地图
     *
     * @param deviceIds 设备id
     * @return {@link Map}<{@link Long}, {@link Map}<{@link Long}, {@link ProductService}>>
     */
    Map<Long, Map<Long, ProductService>> getProfileServiceMap(Set<Long> deviceIds);
}
