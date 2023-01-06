package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

/**
 * @author yefei
 **/
@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}/zabbix/api_jsonrpc.php",
        interceptor = JsonBodyBuildInterceptor.class
)
public interface ZbxInterface {
    /**
     * 查询主机接口
     *
     * @param hostid 主机ID
     */
    @Post
    @JsonPath("/hostinterface/hostinterface.get")
    String hostinterfaceGet(@ParamName("hostid") String hostid);
}
