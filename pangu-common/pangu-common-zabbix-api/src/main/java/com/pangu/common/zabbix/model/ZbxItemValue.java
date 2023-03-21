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
public class ZbxItemValue implements Serializable {

    /**
     * 主机名
     */
    private String host;
    /**
     * item key 例如 temp
     */
    private String key;
    /**
     * item value 例如：90
     */
    private String value;

    /**
     * 如果为 Null，则 zabbix 以接收时间为准
     * 秒，70年到现在时间 例如：System.currentTimeMillis()/1000
     */
    private Long clock;

    /**
     *  纳秒，如果为 Null，则 zabbix 以接收时间为准
     */
    private Long ns;

    /**
     * 设置 数据时间，单独设置 以设备推送的时间数据为准
     *
     * @param clock 秒，70年到现在时间 例如：System.currentTimeMillis()/1000
     * @param ns    纳秒，0-9位数
     */
    public void setTime(Long clock, Long ns) {
        this.clock = clock;
        this.ns = ns;
    }

}
