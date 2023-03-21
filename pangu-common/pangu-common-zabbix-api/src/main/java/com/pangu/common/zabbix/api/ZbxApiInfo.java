package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;

/**
 * zbx api信息
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxApiInfo extends BaseApi{

    /**
     * 接口信息
     *
     * @return String apiinfo
     */
    @Post(headers = "authTag: noAuth")
    @JsonPath("/apiinfo/apiinfo")
    public String getApiInfo();
}
