package com.pangu.common.zabbix.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 发送给zabbix 数据模型
 *
 * @author chengliang
 * @date 2022/11/09
 */
@Data
@Accessors(chain = true)
public class ItemValue implements Serializable {

    /**
     * 主机名
     */
    private String hostname;
    /**
     * item key 例如 temp
     */
    private String itemKey;
    /**
     * item value 例如：90
     */
    private String itemValue;


}
