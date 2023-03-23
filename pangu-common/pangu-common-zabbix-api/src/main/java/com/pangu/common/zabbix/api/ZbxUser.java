package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

import java.util.List;

/**
 * zbx用户
 * <p>
 * ZABBIX User 接口
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxUser extends BaseApi{

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录返回的状态信息
     */
    @Post
    @JsonPath("/user/userLogin")
    String userLogin(@ParamName("username") String username,
                     @ParamName("password") String password);


    /**
     * 用户创建
     *
     * @param name     账号
     * @param password 密码
     * @param usrGrpId 用户组ID
     * @return 用户信息
     */
    @Post
    @JsonPath("/user/userAdd")
    String userAdd(@ParamName("name") String name,
                   @ParamName("password") String password,
                   @ParamName("usrGrpId") String usrGrpId,
                   @ParamName("roleId") String roleId);

    /**
     * 用户修改
     *
     * @param userId   用户id
     * @param usrGrpId 用户组ID
     * @return 用户信息
     */
    @Post
    @JsonPath("/user/userUpdate")
    String userUpdate(@ParamName("userId") String userId,
                      @ParamName("usrGrpId") String usrGrpId,
                      @ParamName("roleId") String roleId);

    /**
     * 用户删除
     *
     * @param usrids 用户id
     * @return 用户信息
     */
    @Post
    @JsonPath("/user/userDelete")
    String userDelete(@ParamName("usrids") List<String> usrids);

    /**
     * 用户修改密码
     *
     * @param userId 用户id
     * @param passwd 用户组ID
     * @return 用户信息
     */
    @Post
    @JsonPath("/user/userUpdatePwd")
    String updatePwd(@ParamName("userId") String userId, @ParamName("passwd") String passwd);

    /**
     * 用户查询
     *
     * @return
     */
    @Post
    @JsonPath("/user/userGet")
    String getUser(@ParamName("userids") String userIds);
}
