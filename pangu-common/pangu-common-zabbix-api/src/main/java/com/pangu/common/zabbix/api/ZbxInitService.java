package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

/**
 * @author nantian created at 2021/8/26 12:20
 */

public interface ZbxInitService {

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/usergroup/cookieUserGroupCreate")
    String createCookieUserGroup(@ParamName("hostGroupId") String hostGroupId, @ParamName("userAuth") String userAuth);

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/usergroup/cookieUserGroupGet")
    String getCookieUserGroup(@ParamName("userAuth") String userAuth);

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/user/cookieUserGet")
    String getCookieUser(@ParamName("userAuth") String userAuth);

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/user/cookieUserAdd")
    String createCookieUser(@ParamName("usrGrpId") String usrGrpId,
                            @ParamName("userAuth") String userAuth,
                            @ParamName("roleId") String roleId);

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/role/adminRole.get")
    String getAdminRole(@ParamName("userAuth") String zbxApiToken);

    @Post(
            url = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
            headers = "authTag: noAuth",
            interceptor = JsonBodyBuildInterceptor.class
    )
    @JsonPath("/role/guestRole.get")
    String getGuestRole(@ParamName("userAuth") String zbxApiToken);
}
