package com.pangu.system.api;

import com.pangu.common.core.domain.dto.ApiTokenDTO;

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


    /**
     * 得到令牌信息牌
     *
     * @param token 令牌
     * @return {@link ApiTokenDTO}
     */
    ApiTokenDTO getTokenInfoByToken(String token);

}
