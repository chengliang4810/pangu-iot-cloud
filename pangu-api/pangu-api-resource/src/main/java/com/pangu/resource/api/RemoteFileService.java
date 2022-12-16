package com.pangu.resource.api;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.resource.api.domain.SysFile;

/**
 * 文件服务
 *
 * @author chengliang4810
 */
public interface RemoteFileService {

    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    SysFile upload(String name, String originalFilename, String contentType, byte[] file) throws ServiceException;
}
