package com.pangu.common.zabbix.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ZbxTag {

    /**
     * 标签名
     */
    private String tag;

    /**
     * 值
     */
    private String value;

}
