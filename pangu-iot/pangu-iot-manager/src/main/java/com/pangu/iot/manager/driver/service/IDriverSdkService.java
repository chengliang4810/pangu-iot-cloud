package com.pangu.iot.manager.driver.service;

import com.pangu.manager.api.domain.DriverMetadata;
import com.pangu.manager.api.domain.dto.DriverDTO;

public interface IDriverSdkService {


    /**
     * 驱动程序注册
     *
     * @param driverDTO 驱动dto
     */
    void driverRegister(DriverDTO driverDTO);

    /**
     * 驱动程序元数据同步
     *
     * @param primaryKey 主键
     */
    DriverMetadata driverMetadataSync(String primaryKey);

}
