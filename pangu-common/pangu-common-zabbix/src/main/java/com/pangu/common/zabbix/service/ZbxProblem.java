package com.pangu.common.zabbix.service;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.inteceptor.JsonBodyBuildInterceptor;

/**
 * @author nantian created at 2021/8/7 23:27
 */

@BaseRequest(baseURL = "http://${zbxServerIp}:${zbxServerPort}${zbxApiUrl}", interceptor = JsonBodyBuildInterceptor.class)
public interface ZbxProblem {


    /**
     * 告警列表
     *
     * @param hostId
     * @return String
     */
    @Post
    @JsonPath("/problem/problem.get")
    String getProblem(@ParamName("hostId") String hostId,
                      @ParamName("timeFrom") Long timeFrom,
                      @ParamName("timeTill") Long timeTill,
                      @ParamName("recent") String recent,
                      @ParamName("severity") String severity);

    @Post
    @JsonPath("/problem/problem.event.get")
    String getEventProblem(@ParamName("hostId") String hostId,
                           @ParamName("timeFrom") Long timeFrom,
                           @ParamName("timeTill") Long timeTill,
                           @ParamName("recent") String recent);

    @Post
    @JsonPath("/problem/problem.acknowledgement")
    String acknowledgement(@ParamName("eventId") String eventId, @ParamName("action") int action);
}
