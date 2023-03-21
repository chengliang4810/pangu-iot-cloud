package com.pangu.iot.data.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于接收zabbix6.4版本的通过http接口发送的事件
 */
@RestController
@RequestMapping("/zabbix/event")
public class ZabbixEventController {

    /**
     * 接收zabbix发送的数据
     */
    @PostMapping("/receive")
    public void receiveEvent(String event) {
        System.out.println(event);
    }

}
