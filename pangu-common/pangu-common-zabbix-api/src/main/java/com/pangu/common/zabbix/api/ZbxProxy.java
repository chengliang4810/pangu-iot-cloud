package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

import java.util.List;

/**
 * zbx代理
 * 代理驱动
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxProxy extends BaseApi {

    /**
     * 创建代理
     *
     * @param name 代理名称
     * @return
     */
    @Post
    @JsonPath("/proxy/proxy.create")
    String proxyCreate(@ParamName("name") String name);


    /**
     * 删除代理
     *
     * @param hostIds 主机ID
     * @return
     */
    @Post
    @JsonPath("/proxy/proxy.delete")
    String proxyDelete(@ParamName("hostIds") List<String> hostIds);

    /**
     * 代理列表
     *
     * @return
     */
    @Post
    @JsonPath("/proxy/proxy.get")
    String get(@ParamName("proxyids") String proxyids);
}
