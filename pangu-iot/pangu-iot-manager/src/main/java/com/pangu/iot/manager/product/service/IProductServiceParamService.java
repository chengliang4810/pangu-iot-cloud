package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductServiceParam;
import com.pangu.iot.manager.product.domain.vo.ProductServiceParamVO;
import com.pangu.iot.manager.product.domain.bo.ProductServiceParamBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 产品功能参数Service接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
public interface IProductServiceParamService extends IService<ProductServiceParam> {

    /**
     * 查询产品功能参数
     */
    ProductServiceParamVO queryById(Long id);

    /**
     * 查询产品功能参数列表
     */
    TableDataInfo<ProductServiceParamVO> queryPageList(ProductServiceParamBO bo, PageQuery pageQuery);

    /**
     * 查询产品功能参数列表
     */
    List<ProductServiceParamVO> queryList(ProductServiceParamBO bo);

    /**
     * 修改产品功能参数
     */
    Boolean insertByBo(ProductServiceParamBO bo);

    /**
     * 修改产品功能参数
     */
    Boolean updateByBo(ProductServiceParamBO bo);

    /**
     * 校验并批量删除产品功能参数信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
