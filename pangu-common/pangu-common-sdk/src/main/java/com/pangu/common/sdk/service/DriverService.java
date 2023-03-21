package com.pangu.common.sdk.service;


import com.pangu.common.core.domain.dto.DeviceExecuteResult;

/**
 * 驱动服务
 *
 * @author chengliang
 * @date 2023/03/02
 */
public interface DriverService {

    /**
     * 驱动程序元数据同步
     *
     * @param primaryKey 主键
     */
    void driverMetadataSync(String primaryKey);


    void notifyDeviceFunctionResult(DeviceExecuteResult deviceExecuteResult);

    /**
     * Close ApplicationContext
     *
     * @param template Template
     * @param params   Object Params
     */
    void close(CharSequence template, Object... params);


}
