package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

import java.util.List;

/**
 * @author yefei
 * <p>
 * 代理驱动
 */
@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}/zabbix/api_jsonrpc.php",
        interceptor = JsonBodyBuildInterceptor.class
)
public interface ZbxProxy {

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
