package com.pangu.system.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.system.api.RemoteTokenService;
import com.pangu.system.api.model.ApiTokenDTO;
import com.pangu.system.convert.ApiTokenConvert;
import com.pangu.system.domain.ApiToken;
import com.pangu.system.service.IApiTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录
 *
 * @author chengliang4810
 */
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteTokenServiceImpl implements RemoteTokenService {

    private final IApiTokenService tokenService;
    private final ApiTokenConvert apiTokenConvert;

    /**
     * 通过token查询 token信息
     *
     * @param token token信息
     * @return 结果
     */
    @Override
    public ApiTokenDTO getTokenInfo(String token) {
        ApiToken apiToken = tokenService.getOne(Wrappers.lambdaQuery(ApiToken.class).eq(ApiToken::getToken, token).last("limit 1"));
        return apiTokenConvert.toDTO(apiToken);
    }


}
