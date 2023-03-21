package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import com.dtflys.forest.annotation.Var;
import com.pangu.common.zabbix.entity.SendParam;

/**
 * @author yefei
 */

public interface ZbxConfig {


    /**
     * 读取 zbx 配置信息
     *
     * @return String
     */
    @Post(url = "http://127.0.0.1:12800/zabbix/config")
    String getConfig();

    /**
     * 读取 zbx 配置信息
     *
     * @return String
     */
    @Post(url = "http://${ip}:12800/device/attr/send")
    String sendData(@Var("ip") String ip, @JSONBody SendParam sendParam);

}
