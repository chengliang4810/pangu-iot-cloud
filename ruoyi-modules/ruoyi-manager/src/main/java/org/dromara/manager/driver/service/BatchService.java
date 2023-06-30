package org.dromara.manager.driver.service;

import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.manager.driver.domain.vo.DriverVo;

import java.util.List;

public interface BatchService {

    /**
     * 批处理驱动程序元数据
     *
     * @param code 代码
     * @return {@link DriverMetadata}
     */
    DriverMetadata batchDriverMetadata(String code);

    List<DriverVo> queryParentDeviceDriver(Long deviceId);
}
