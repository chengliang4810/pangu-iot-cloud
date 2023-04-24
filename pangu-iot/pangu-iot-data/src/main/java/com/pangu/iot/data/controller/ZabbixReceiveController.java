package com.pangu.iot.data.controller;

import com.pangu.common.core.domain.dto.ZabbixEventDTO;
import com.pangu.common.core.domain.dto.ZabbixItemDTO;
import com.pangu.iot.data.service.ZabbixReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/zabbix/receive")
public class ZabbixReceiveController {

    private final ZabbixReceiveService zabbixReceiveService;

    /**
     * 接收数据
     *
     * @param zabbixItemList
     */
    @PostMapping(value = "/item",  consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public void receiveData(@RequestBody List<ZabbixItemDTO> zabbixItemList) {
        try {
            zabbixItemList.forEach(zabbixReceiveService::receiveItemData);
        } catch (Exception e) {
            log.warn("接收zabbix数据失败", e);
        }
    }

    /**
     * 接收事件
     * 接收zabbix发送的数据
     *
     * @param zabbixEventList
     */
    @PostMapping(value = "/event",  consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public void receiveEvent(@RequestBody List<ZabbixEventDTO> zabbixEventList) {
        try {
            zabbixEventList.forEach(zabbixReceiveService::receiveEventData);
        } catch (Exception e) {
            log.warn("接收zabbix事件失败", e);
        }
    }

}
