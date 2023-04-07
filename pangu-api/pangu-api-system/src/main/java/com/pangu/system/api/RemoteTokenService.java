package com.pangu.system.api;

import com.pangu.system.api.model.ApiTokenDTO;

import java.util.List;

/**
 * ApiToken服务
 *
 * @author chengliang4810
 * @date 2023/03/14
 */
public interface RemoteTokenService {

    /**
     * 得到令牌信息列表
     *
     * @return {@link List}<{@link ApiTokenDTO}>
     */
    List<ApiTokenDTO> getTokenInfoList();

}
