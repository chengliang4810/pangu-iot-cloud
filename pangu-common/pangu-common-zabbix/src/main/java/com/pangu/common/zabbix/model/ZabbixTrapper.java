package com.pangu.common.zabbix.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * zabbix 发送数据模型
 *
 * @author chengliang4810
 * @date 2023/01/09 10:58
 */
public class ZabbixTrapper {

    @Getter
    private final String request = "sender data";

    @Setter
    @Getter
    private List<ZbxItemValue> data;

    public ZabbixTrapper(List<ZbxItemValue> itemValues) {
        data = new ArrayList<>();
        data.addAll(itemValues);
    }

}
