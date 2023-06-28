package org.dromara.manager.product.service;

import java.util.Collection;

/**
 * 产品批量处理Service接口
 */
public interface IProductBatchService {

    /**
     * 校验并批量删除产品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
