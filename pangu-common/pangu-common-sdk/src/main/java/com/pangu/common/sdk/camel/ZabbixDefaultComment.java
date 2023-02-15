package com.pangu.common.sdk.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.support.DefaultComponent;

import java.util.Map;

public class ZabbixDefaultComment extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // parameters 里面的参数 必须要清空
        String value = getAndRemoveParameter(parameters, "method", String.class);
        ZabbixSenderEndpoint zabbixSenderEndpoint = new ZabbixSenderEndpoint(uri, this);
        zabbixSenderEndpoint.setEndpointUriIfNotSpecified("zabbix");
        return  zabbixSenderEndpoint;
    }

}
