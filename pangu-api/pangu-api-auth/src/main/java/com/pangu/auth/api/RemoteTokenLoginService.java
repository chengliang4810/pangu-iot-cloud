package com.pangu.auth.api;

/**
 * ApiToken服务
 *
 * @author chengliang4810
 * @date 2023/03/14
 */
public interface RemoteTokenLoginService {

    /**
     * 得到令牌信息牌
     *
     * @param token 令牌
     */
    void getTokenInfoByToken(String token);

}
