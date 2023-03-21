package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

import java.util.List;

/**
 * zbx历史数据
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxHistoryGet extends BaseApi {

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
