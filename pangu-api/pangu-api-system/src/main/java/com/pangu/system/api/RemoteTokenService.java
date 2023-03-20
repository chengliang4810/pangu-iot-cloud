package com.pangu.system.api;

import com.pangu.system.api.model.ApiTokenDTO;

/**
 * ApiToken服务
 *
 * @author chengliang4810
 * @date 2023/03/14
 */
public interface RemoteTokenService {

    /**
     * 通过token查询 token信息
     *
     * @param token token信息
     * @return 结果
     */
    ApiTokenDTO getTokenInfo(String token);

}
