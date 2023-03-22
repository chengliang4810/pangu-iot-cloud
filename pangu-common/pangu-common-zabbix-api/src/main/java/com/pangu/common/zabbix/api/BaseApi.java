package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
        headers = {
            "Content-Type: application/json-rpc",
            "Authorization: Bearer ${zbxApiToken}",
        },
        interceptor = JsonBodyBuildInterceptor.class
)
public interface BaseApi {
}
