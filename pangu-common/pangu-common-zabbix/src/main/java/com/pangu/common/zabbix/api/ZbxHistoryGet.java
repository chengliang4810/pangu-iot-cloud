package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

import java.util.List;

/**
 * @author nantian created at 2021/8/3 14:50
 */

@BaseRequest(
        baseURL = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}",
        interceptor = JsonBodyBuildInterceptor.class
)
public interface ZbxHistoryGet {

    @Post
    @JsonPath("/history/history.get")
    String historyGet(@ParamName("hostid") String hostid,
                      @ParamName("itemids") List<String> itemids,
                      @ParamName("hisNum") Integer hisNum,
                      @ParamName("valueType") Integer valueType,
                      @ParamName("timeFrom") Long timeFrom,
                      @ParamName("timeTill") Long timeTill);

    @Post(headers = "authTag: noAuth")
    @JsonPath("/history/history.get")
    String historyGetWithNoAuth(@ParamName("hostid") String hostid,
                                @ParamName("itemids") List<String> itemids,
                                @ParamName("hisNum") Integer hisNum,
                                @ParamName("valueType") Integer valueType,
                                @ParamName("userAuth") String zbxApiToken);
}
