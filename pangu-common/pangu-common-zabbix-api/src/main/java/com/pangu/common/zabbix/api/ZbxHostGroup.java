package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

import java.util.List;

/**
 * zbx主机组
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxHostGroup extends BaseApi {


    /**
     * 获取 全局 主机组
     *
     * @param userAuth api token
     * @return String
     */
    @Post()
    @JsonPath("/hostgroup/hostgroup.global.get")
    String getGlobalHostGroup(@ParamName("userAuth") String userAuth);


    /**
     * 创建默认全局主机组
     *
     * @param userAuth userToken
     * @return String
     */
    @Post()
    @JsonPath("/hostgroup/hostgroup.init.create")
    String createGlobalHostGroup(@ParamName("userAuth") String userAuth);

    /**
     * 创建主机组
     *
     * @param hostGroupName 主机组名称
     * @return String
     */
    @Post
    @JsonPath("/hostgroup/hostgroup.create")
    String hostGroupCreate(@ParamName("hostGroupName") String hostGroupName);


    /**
     * 删除主机组
     *
     * @param hostGrpIds 主机组IDS
     * @return String
     */
    @Post
    @JsonPath("/hostgroup/hostgroup.delete")
    String hostGroupDelete(@ParamName("hostGroupIds") List<String> hostGrpIds);

    /**
     * 获取 主机组
     *
     * @return String
     */
    @Post
    @JsonPath("/hostgroup/hostgroup.get")
    String getHostGroup(@ParamName("groupids") String groupids);
}
