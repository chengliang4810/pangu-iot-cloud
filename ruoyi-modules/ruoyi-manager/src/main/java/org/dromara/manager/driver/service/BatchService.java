package org.dromara.manager.driver.service;

import org.dromara.common.iot.entity.driver.DriverMetadata;

public interface BatchService {

    /**
     * 批处理驱动程序元数据
     *
     * @param code 代码
     * @return {@link DriverMetadata}
     */
    DriverMetadata batchDriverMetadata(String code);

}
