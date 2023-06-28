package org.dromara.manager.device.service;

import java.util.Collection;

/**
 * 批量处理设备服务
 *
 * @author chengliang
 * @date 2023/06/29
 */
public interface IDeviceBatchService {

    /**
     * 校验并批量删除设备信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
