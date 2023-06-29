package org.dromara.manager.product.service;

import org.dromara.manager.product.domain.vo.ProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 产品批量处理Service接口
 */
public interface IProductBatchService {

    /**
     * 校验并批量删除产品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<ProductVo> queryParentDeviceProduct(Long deviceId);
}
