package com.pangu.system.dubbo;

import com.pangu.system.api.RemoteTokenService;
import com.pangu.system.api.model.ApiTokenDTO;
import com.pangu.system.convert.ApiTokenConvert;
import com.pangu.system.domain.ApiToken;
import com.pangu.system.service.IApiTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 得到令牌信息列表
     *
     * @return {@link List}<{@link ApiTokenDTO}>
     */
    @Override
    public List<ApiTokenDTO> getTokenInfoList() {
        List<ApiToken> apiTokenList = tokenService.list();
        return apiTokenConvert.toDtoList(apiTokenList);
    }

}
