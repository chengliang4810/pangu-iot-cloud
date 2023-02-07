package com.pangu.iot.manager.product.service;

import com.pangu.iot.manager.product.domain.ProductEventService;
import com.pangu.iot.manager.product.domain.vo.ProductEventServiceVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventServiceBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 告警规则与功能关系Service接口
 *
 * @author chengliang4810
 * @date 2023-02-07
 */
public interface IProductEventServiceService extends IService<ProductEventService> {

    /**
     * 查询告警规则与功能关系
     */
    ProductEventServiceVO queryById(Long id);

    /**
     * 查询告警规则与功能关系列表
     */
    TableDataInfo<ProductEventServiceVO> queryPageList(ProductEventServiceBO bo, PageQuery pageQuery);

    /**
     * 查询告警规则与功能关系列表
     */
    List<ProductEventServiceVO> queryList(ProductEventServiceBO bo);

    /**
     * 修改告警规则与功能关系
     */
    Boolean insertByBo(ProductEventServiceBO bo);

    /**
     * 修改告警规则与功能关系
     */
    Boolean updateByBo(ProductEventServiceBO bo);

    /**
     * 校验并批量删除告警规则与功能关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
