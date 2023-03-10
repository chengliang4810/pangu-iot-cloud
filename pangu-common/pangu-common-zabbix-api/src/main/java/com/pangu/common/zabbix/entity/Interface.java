package com.pangu.common.zabbix.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yefei
 **/
@Data
@Accessors(chain = true)
public class Interface {

    private String interfaceid;

    private String hostid;

    private String deviceId;

    private String main = "1";

    private String type = "1";

    private String useip="1";

    private String ip;

    private String dns="";

    private String port;

    private String avaiable;

    private String error;

    private String errors_from;

    private String disable_until;
}
