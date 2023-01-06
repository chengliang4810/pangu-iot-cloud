package com.pangu.iot.manager.device.service;

import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;

import java.util.Collection;

/**
 * 产品与属性通用处理部分
 */
public interface IProductAndAttributeService {

    /**
     * 新增属性
     *
     * @param attributeBo 属性
     * @return {@link Boolean}
     */
    Boolean insertAttribute(DeviceAttributeBO attributeBo);

    /**
     * 更新属性
     *
     * @param attributeBo 属性
     * @return {@link Boolean}
     */
    Boolean updateAttribute(DeviceAttributeBO attributeBo);


    /**
     * 删除属性
     *
     * @param ids id
     * @return {@link Boolean}
     */
    Boolean deleteAttributeByIds(Collection<Long> ids);

    /**
     * 删除产品
     *
     * @param ids id
     * @return {@link Boolean}
     */
    Boolean deleteProductByIds(Collection<Long> ids);

}
