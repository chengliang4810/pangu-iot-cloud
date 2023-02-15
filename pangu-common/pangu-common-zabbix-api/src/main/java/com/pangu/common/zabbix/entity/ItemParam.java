package com.pangu.common.zabbix.entity;

import lombok.Data;

/**
 * @author yefei
 **/
@Data
public class ItemParam {
    private String host;
    private String key;
    private String value;
    private Long   clock;
}
