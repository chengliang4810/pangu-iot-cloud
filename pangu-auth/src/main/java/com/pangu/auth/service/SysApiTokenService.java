package com.pangu.auth.service;

import com.pangu.common.core.domain.dto.ApiTokenDTO;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.satoken.utils.LoginHelper;
import com.pangu.system.api.RemoteTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysApiTokenService {

    @DubboReference(check = false)
    private final RemoteTokenService remoteTokenService;

    /**
     * 注册所有第三方平台Token
     */
    public void registerApiToken(String token) {
        ApiTokenDTO apiToken = remoteTokenService.getTokenInfoByToken(token);
        Assert.notNull(apiToken, "令牌不存在");
        Assert.isTrue(apiToken.getStatus(), "令牌已被禁用");
        Assert.isTrue(apiToken.getExpirationTime() == null || System.currentTimeMillis() - apiToken.getExpirationTime().getTime() <= 0, "令牌已过期");
        LoginHelper.loginByApiToken(apiToken);
    }

}
