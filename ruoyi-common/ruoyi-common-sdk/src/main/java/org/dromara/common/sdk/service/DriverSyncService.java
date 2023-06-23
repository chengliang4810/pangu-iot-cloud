package org.dromara.common.sdk.service;


import org.dromara.common.iot.dto.DriverSyncDownDTO;

/**
 * 驱动同步相关接口
 *
 * @author pnoker, chengliang4810
 * @date 2023/06/23
 */
public interface DriverSyncService {

    /**
     * 同步驱动信息到平台端
     */
    void up();

    /**
     * 同步平台端信息到驱动
     *
     * @param entityDTO DriverSyncDTO
     */
    void down(DriverSyncDownDTO entityDTO);
}
