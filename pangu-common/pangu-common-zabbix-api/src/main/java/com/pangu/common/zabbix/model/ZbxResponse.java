package com.pangu.common.zabbix.model;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Accessors(chain = true)
public class ZbxResponse {

    /**
     * 完成总条数
     */
    private Integer total;

    /**
     * 失败条数
     */
    private Integer failed;

    /**
     * response success(成功);
     */
    private String response;


}
