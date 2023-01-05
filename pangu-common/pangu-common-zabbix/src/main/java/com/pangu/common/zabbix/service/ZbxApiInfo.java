package com.pangu.common.zabbix.service;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

/**
 * @author nantian created at 2021/8/3 11:58
 */

@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
        interceptor = JsonBodyBuildInterceptor.class
)
public interface ZbxApiInfo {

    /**
     * 接口信息
     *
     * @return String apiinfo
     */
    @Post(headers = "authTag: noAuth")
    @JsonPath("/apiinfo/apiinfo")
    public String getApiInfo();
}
