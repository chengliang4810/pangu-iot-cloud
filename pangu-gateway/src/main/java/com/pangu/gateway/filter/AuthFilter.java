package com.pangu.gateway.filter;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.constant.HttpStatus;
import com.pangu.gateway.config.properties.IgnoreWhiteProperties;
import com.pangu.system.api.RemoteTokenService;
import com.pangu.system.api.model.ApiTokenDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * [Sa-Token 权限认证] 拦截器
 *
 * @author chengliang4810
 */
@Configuration
public class AuthFilter {

    @Resource
    @DubboReference
    private RemoteTokenService tokenService;

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
                // 登录校验 -- 拦截所有路由
                SaRouter.match("/**")
                    .notMatch(ignoreWhite.getWhites())
                    .check(r -> {
                        // 检查是否登录 是否有token
                        try {
                            StpUtil.checkLogin();
                        } catch (NotLoginException e) {
                            // redis 不存在该token
                            if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
                                // 调用system服务查询token是否存在
                                ApiTokenDTO tokenInfo = tokenService.getTokenInfo(StpUtil.getTokenValue());
                                if (tokenInfo == null) {
                                    throw e;
                                }

                                long timeout = SaManager.getConfig().getTimeout();
                                // 计算token剩余有效时间 单位秒
                                if (ObjectUtil.isNotNull(tokenInfo.getExpirationTime())){
                                    timeout = (tokenInfo.getExpirationTime().getTime() - System.currentTimeMillis()) / 1000;
                                }
                                StpUtil.login(tokenInfo.getId(), timeout);
                                return;
                            }
                            throw e;
                        }

                        // 有效率影响 用于临时测试
                        // if (log.isDebugEnabled()) {
                        //     log.debug("剩余有效时间: {}", StpUtil.getTokenTimeout());
                        //     log.debug("临时有效时间: {}", StpUtil.getTokenActivityTimeout());
                        // }
                    });
            }).setError(e -> SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED));
    }
}
