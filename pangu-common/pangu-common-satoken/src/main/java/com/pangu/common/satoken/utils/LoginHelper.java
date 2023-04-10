package com.pangu.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.constant.UserConstants;
import com.pangu.common.core.domain.dto.ApiTokenDTO;
import com.pangu.common.core.enums.DeviceType;
import com.pangu.common.core.enums.UserType;
import com.pangu.common.core.exception.UtilException;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.system.api.model.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录鉴权助手
 *
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 *
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author chengliang4810
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String JOIN_CODE = ":";
    public static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        System.out.println(33333);
        StpUtil.login(loginUser.getLoginId());
        setLoginUser(loginUser);
    }

    /**
     * 登录通过api令牌
     *
     * @param apiTokenDTO api牌dto
     */
    public static void loginByApiToken(ApiTokenDTO apiTokenDTO) {
        System.out.println("loginByApiToken： " + apiTokenDTO);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(apiTokenDTO.getId());
        loginUser.setToken(apiTokenDTO.getToken());
        loginUser.setUsername(apiTokenDTO.getName());
        loginUser.setUserType(UserType.THIRD_PARTY.getUserType());

        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        System.out.println(33333);
        try {
            StpUtil.login(loginUser.getLoginId(), new SaLoginModel().setTimeout(-1).setToken(apiTokenDTO.getToken()).setDevice(DeviceType.THIRD_PARTY.getDevice()));
            System.out.println(22222222);
        } catch (Exception e) {
            System.out.println(444444);
            e.printStackTrace();
        }
        System.out.println(11111);
        setLoginUser(loginUser);
        System.out.println("loginByApiToken222： " + loginUser);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, DeviceType deviceType) {
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        StpUtil.login(loginUser.getLoginId(), deviceType.getDevice());
        setLoginUser(loginUser);
    }

    /**
     * 设置用户数据(多级缓存)
     */
    public static void setLoginUser(LoginUser loginUser) {
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (ObjectUtil.isNull(loginUser)) {
            String loginId = StpUtil.getLoginIdAsString();
            String[] strs = StringUtils.split(loginId, JOIN_CODE);
            if (!ArrayUtil.containsAny(strs, UserType.values())) {
                throw new UtilException("登录用户: LoginId异常 => " + loginId);
            }
            // 用户id在总是在最后
            return Long.parseLong(strs[strs.length - 1]);
        }
        return loginUser.getUserId();
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户类型
     */
    public static UserType getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserType.getUserType(loginId);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }


}
