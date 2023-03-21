package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

/**
 * zbx问题
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxProblem extends BaseApi {


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
