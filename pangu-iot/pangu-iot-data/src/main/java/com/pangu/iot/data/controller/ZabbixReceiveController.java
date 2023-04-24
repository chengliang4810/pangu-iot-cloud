package com.pangu.iot.data.controller;

import com.pangu.common.core.domain.dto.ZabbixEventDTO;
import com.pangu.common.core.domain.dto.ZabbixItemDTO;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.iot.data.service.ZabbixReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * @param body 身体
     */
    @PostMapping("/item")
    public void receiveData(@RequestBody String body) {
        List<ZabbixItemDTO> zabbixItemList = JsonUtils.parseNdjson(body, ZabbixItemDTO.class);
        zabbixItemList.forEach(zabbixReceiveService::receiveItemData);
    }


    /**
     * 接收事件
     * 接收zabbix发送的数据
     *
     * @param body 身体
     */
    @PostMapping("/event")
    public void receiveEvent(@RequestBody String body) {
        List<ZabbixEventDTO> zabbixItemList = JsonUtils.parseNdjson(body, ZabbixEventDTO.class);
        zabbixItemList.forEach(zabbixReceiveService::receiveEventData);
    }

}
