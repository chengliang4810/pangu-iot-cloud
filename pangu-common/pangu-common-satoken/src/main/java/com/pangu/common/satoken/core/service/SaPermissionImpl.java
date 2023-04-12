package com.pangu.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.pangu.common.core.enums.UserType;
import com.pangu.common.satoken.utils.LoginHelper;
import com.pangu.system.api.model.LoginUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author chengliang4810
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.SYS_USER) {
            return new ArrayList<>(loginUser.getMenuPermission());
        } else if (userType == UserType.APP_USER) {
            // 其他端 自行根据业务编写
        } else if (userType == UserType.THIRD_PARTY) {
            // 其他端 自行根据业务编写
            return Collections.singletonList("*:*:*");
        }
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.SYS_USER) {
            return new ArrayList<>(loginUser.getRolePermission());
        } else if (userType == UserType.APP_USER) {
            // 其他端 自行根据业务编写
        } else if (userType == UserType.THIRD_PARTY) {
            // 第三方平台 自行根据业务编写
            return Collections.singletonList("admin");
        }
        return new ArrayList<>();
    }
}
