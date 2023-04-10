package com.pangu.auth.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.pangu.auth.service.SysLoginService;
import com.pangu.common.core.enums.UserType;
import com.pangu.system.api.RemoteTokenService;
import com.pangu.system.api.RemoteUserService;
import com.pangu.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * init Token 处理程序
 *
 * @author chengliang
 * @date 2023/04/07
 */
@Slf4j
@Configuration
public class InitTokenHandler implements ApplicationRunner {

    @Resource
    private SysLoginService sysLoginService;
    @DubboReference
    private RemoteUserService userService;
    @DubboReference
    private RemoteTokenService remoteTokenService;

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LoginUser admin = userService.getUserInfo("admin");
        System.out.println(admin);
//        List<ApiTokenDTO> tokenInfoList = remoteTokenService.getTokenInfoList();
//        ApiTokenDTO apiTokenDTO = tokenInfoList.get(0);
//        LoginUser loginUser = new LoginUser();
//        loginUser.setUserId(apiTokenDTO.getId());
//        loginUser.setUserType(UserType.THIRD_PARTY.getUserType());
//        loginUser.setUsername(apiTokenDTO.getName());

        //SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        ThreadUtil.execAsync(() -> {
            System.out.println(33333);
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(1L);
            loginUser.setUserType(UserType.THIRD_PARTY.getUserType());
            StpUtil.login(loginUser.getLoginId());
            System.out.println(11111);
            //LoginHelper.loginByDevice(loginUser, DeviceType.PC);
            log.info("register api token success");
        });
    }

}
