package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

public interface ZbxTokenApi extends BaseApi{

    /**
     * 创建Token
     */
    @Post
    @JsonPath("/token/token.create")
    String createToken(@ParamName("userId") String userId, @ParamName("tokenName") String name);

    /**
     * 生成对应Token
     */
    @Post
    @JsonPath("/token/token.generate")
    String generateToken(@ParamName("tokenId") String tokenId);

}
