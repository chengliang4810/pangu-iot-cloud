package com.pangu.system.dubbo;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.system.api.RemoteTokenService;
import com.pangu.common.core.domain.dto.ApiTokenDTO;
import com.pangu.system.convert.ApiTokenConvert;
import com.pangu.system.domain.ApiToken;
import com.pangu.system.service.IApiTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author chengliang4810
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteTokenServiceImpl implements RemoteTokenService {

    private final IApiTokenService tokenService;
    private final ApiTokenConvert apiTokenConvert;

    /**
     * 得到令牌信息列表
     *
     * @return {@link List}<{@link ApiTokenDTO}>
     */
    @Override
    public List<ApiTokenDTO> getTokenInfoList() {
        List<ApiToken> apiTokenList = tokenService.list();
        log.info("apiTokenList: {}", apiTokenList);
        return apiTokenConvert.toDtoList(apiTokenList);
    }

    /**
     * 得到令牌信息牌
     *
     * @param token 令牌
     * @return {@link ApiTokenDTO}
     */
    @Override
    public ApiTokenDTO getTokenInfoByToken(String token) {
        ApiToken apiToken = tokenService.getOne(Wrappers.lambdaQuery(ApiToken.class).eq(ApiToken::getToken, token).last("limit 1"));
        if (apiToken == null) {
            return null;
        }
        return apiTokenConvert.toDTO(apiToken);
    }
}
