package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

import java.util.List;

/**
 * @author nantian created at 2021/8/2 13:09
 * <p>
 * ZABBIX UserGrp 接口
 */

@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
        interceptor = JsonBodyBuildInterceptor.class
)
public interface ZbxUserGroup {


    /**
     * 用户组创建
     *
     * @param userGroupName 名称
     * @return 用户组信息
     */
    @Post
    @JsonPath("/usergroup/userGroupCreate")
    String userGrpAdd(@ParamName("userGroupName") String userGroupName);


    /**
     * 用户组删除
     *
     * @param usrGrpIds 用户组id
     */
    @Post
    @JsonPath("/usergroup/userGroupDelete")
    String userGrpDelete(@ParamName("usrgrpids") List<String> usrGrpIds);


    /**
     * 用户组绑定主机组
     *
     * @param hostGroupIds 主机组IDs 修改和删除 都是这个方法
     * @param userGroupId  用户组ID
     * @return
     */
    @Post
    @JsonPath("/usergroup/userGroupBindHostGroup")
    String userGrpBindHostGroup(@ParamName("hostGroupIds") List<String> hostGroupIds,
                                @ParamName("userGroupId") String userGroupId);

    @Post
    @JsonPath("/usergroup/userGroupGet")
    String getUserGrp(@ParamName("usrgrpids") String usrgrpids);
}
