package com.pangu.common.zabbix.entity;

import lombok.Data;

import java.util.List;

/**
 * @author yefei
 **/
@Data
public class SendParam {
    private List<ItemParam> params;
}
