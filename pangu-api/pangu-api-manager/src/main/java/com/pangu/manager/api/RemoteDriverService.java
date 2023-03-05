package com.pangu.manager.api;

import com.pangu.manager.api.domain.dto.DriverDTO;

/**
 * 远程驱动服务
 *
 * @author chengliang
 * @date 2023/02/28
 */
public interface RemoteDriverService {

    /**
     * 驱动程序注册
     *
     * @param driverDto 驱动dto
     */
    void driverRegister(DriverDTO driverDto);


    /**
     * 驱动程序元数据同步
     *
     * @param primaryKey 主键
     */
    void driverMetadataSync(String primaryKey);


}
