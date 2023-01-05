package com.pangu.common.zabbix.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Zabbix反馈的实时数据
 *
 * @author chengliang
 * @date 2022/11/09
 */
@Data
@Accessors(chain = true)
public class ItemValueResult {

    /**
     * zabbix 返回的实时数据
     * {"type":3,"value":50,"groups":["1584788780993126400"],"ns":0,"name":"1584742231894302720","hostname":"1584789431676477440","clock":1667897914,"item_tags":[],"itemid":36665}
     */
    private Integer itemid;
    /**
     * item Name
     */
    private String name;
    /**
     * item value
     *【设备上报 值】，都是文本
     * Zabbix 会根据配置的ITEM 类型，进行转换，如果失败就报错
     */
    private String value;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 分组
     */
    private List<String> groups;
    /**
     * 时间
     */
    private Integer clock;
    /**
     * 标签
     */
    private List<String> itemTags;

}
