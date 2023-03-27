package com.pangu.iot.data.service;

import com.pangu.common.core.domain.dto.ZabbixEventDTO;
import com.pangu.common.core.domain.dto.ZabbixItemDTO;

/**
 * zabbix接收服务
 *
 * @author chengliang
 * @date 2023/03/27
 */
public interface ZabbixReceiveService {

    /**
     * 接收监控项数据
     */
    void receiveItemData(ZabbixItemDTO zabbixItem);


    /**
     * 接收事件数据
     */
    void receiveEventData(ZabbixEventDTO zabbixEvent);

}
