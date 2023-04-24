package com.pangu.iot.data.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pangu.common.core.domain.dto.ZabbixEventDTO;
import com.pangu.common.core.domain.dto.ZabbixItemDTO;
import com.pangu.iot.data.service.ZabbixReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            // 使用换行分割字符串 TODO: 2021/3/31 ndjson 格式。 后续需要优化为Map或List的格式
            StrUtil.split(body, StrUtil.C_LF).forEach(json ->{
                log.debug("接收到zabbix数据: {}", json);
                if (StrUtil.isBlank(json)) {
                    return;
                }
                ZabbixItemDTO zabbixItemDTO = JSONUtil.toBean(json, ZabbixItemDTO.class);
                zabbixReceiveService.receiveItemData(zabbixItemDTO);
            });
        } catch (Exception e) {
            log.warn("接收zabbix数据失败", e);
        }
    }

    /**
     * 接收事件
     * 接收zabbix发送的数据
     *
     * @param body 身体
     */
    @PostMapping("/event")
    public void receiveEvent(@RequestBody String body) {
        try {
            // 使用换行分割字符串
            StrUtil.split(body, StrUtil.C_LF).forEach(json ->{
                if (StrUtil.isBlank(json)) {
                    return;
                }
                ZabbixEventDTO zabbixEventDTO = JSONUtil.toBean(json, ZabbixEventDTO.class);
                zabbixReceiveService.receiveEventData(zabbixEventDTO);
            });
        } catch (Exception e) {
            log.warn("接收zabbix事件失败", e);
        }
    }

}
