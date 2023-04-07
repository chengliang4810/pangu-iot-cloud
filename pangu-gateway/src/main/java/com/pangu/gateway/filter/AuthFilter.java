package com.pangu.gateway.filter;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.pangu.common.core.constant.HttpStatus;
import com.pangu.gateway.config.properties.IgnoreWhiteProperties;
import com.pangu.system.api.RemoteTokenService;
import com.pangu.system.api.model.ApiTokenDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * [Sa-Token 权限认证] 拦截器
 *
 * @author chengliang4810
 */
@Configuration
@RequiredArgsConstructor
public class AuthFilter {

    @DubboReference
    private final RemoteTokenService tokenService;

    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter(IgnoreWhiteProperties ignoreWhite) {
        return new SaReactorFilter()
            // 拦截地址
            .addInclude("/**")
            .addExclude("/favicon.ico", "/actuator/**")
            // 鉴权方法：每次访问进入
            .setAuth(obj -> {

                List<ApiTokenDTO> tokenInfoList = tokenService.getTokenInfoList();
                System.out.println(tokenInfoList + "----------------");
                // 获取登录token
                String token = StpUtil.getTokenValue();
                // 比对token
                for (ApiTokenDTO apiTokenDTO : tokenInfoList) {
                    System.out.println(apiTokenDTO.getToken() + "-----" + token);
                    if (apiTokenDTO.getToken().equals(token)) {
                        // 注册token
                        StpUtil.login(token, -1);
                        break;
                    }
                }

                // 登录校验 -- 拦截所有路由
                SaRouter.match("/**")
                    .notMatch(ignoreWhite.getWhites())
                    .check(r -> {

                        // 检查是否登录 是否有token
                        StpUtil.checkLogin();
                        // 有效率影响 用于临时测试
                        // if (log.isDebugEnabled()) {
                        //     log.debug("剩余有效时间: {}", StpUtil.getTokenTimeout());
                        //     log.debug("临时有效时间: {}", StpUtil.getTokenActivityTimeout());
                        // }
                    });
            })
            // 认证函数之前执行
           .setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));
    }
}
