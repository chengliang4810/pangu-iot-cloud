package org.dromara.manager.driver.service;

import org.dromara.common.iot.dto.DriverSyncUpDTO;

public interface DriverSyncService {


    /**
     * 驱动注册上线
     *
     * @param entityDTO 实体dto
     */
    void up(DriverSyncUpDTO entityDTO);

}
