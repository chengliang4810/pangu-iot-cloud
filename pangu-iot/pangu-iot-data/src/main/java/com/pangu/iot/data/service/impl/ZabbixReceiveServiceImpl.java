package com.pangu.iot.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.domain.dto.*;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import com.pangu.iot.data.service.ZabbixReceiveService;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.SUPER_TABLE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZabbixReceiveServiceImpl implements ZabbixReceiveService {

    private final TdEngineService tdEngineService;
    private final DeviceStatusService deviceStatusService;
    private final EmqxClient emqxClient;

    /**
     * 接收监控项数据
     */
    @Override
    public void receiveItemData(ZabbixItemDTO zabbixItem) {

        if (ObjectUtil.isNull(zabbixItem)) {
            return;
        }

        String hostname = zabbixItem.getHost().getName();
        // 过滤Zabbix server不处理, 后续可以单独处理加入到大屏展示数据中
        if ("Zabbix server".equals(zabbixItem.getHost().getName())) {
            return;
        }

        log.debug("接收到ZBX数据：{}", zabbixItem);
        Map<String, String> tagMap = zabbixItem.getTags().stream().collect(Collectors.toMap(TagsDTO::getTag, TagsDTO::getValue));
        if (!tagMap.containsKey(IotConstants.PRODUCT_ID_TAG_NAME)) {
            log.warn("未知产品：{}", zabbixItem);
            return;
        }
        // 存入tdengine
        String productId = tagMap.get(IotConstants.PRODUCT_ID_TAG_NAME);
        String attributeKey = tagMap.get(IotConstants.ATTRIBUTE_KEY_TAG_NAME);
        // 获取当前设备状态
        boolean status = deviceStatusService.getOnlineStatus(hostname);

        Map<String, Object> value = new LinkedHashMap<>(3);
        value.put(IotConstants.TABLE_STATUS_FIELD, status ? 1 : 0);
        value.put(attributeKey, zabbixItem.getValue());
        value.put(IotConstants.TABLE_PRIMARY_FIELD, TimeUtil.toTimestamp(zabbixItem.getClock(), zabbixItem.getNs()));
        tdEngineService.insertData(IotConstants.DEVICE_TABLE_NAME_PREFIX + hostname, SUPER_TABLE_PREFIX + productId, value);
        // 发送到EMQX
        MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(hostname, zabbixItem.getName(), zabbixItem.getValue().toString(), zabbixItem.getClock());
        emqxClient.publish("iot/device/" + hostname + "/attribute/" + zabbixItem.getName(), JsonUtils.toJsonStringBytes(mqttMessageDTO));

    }

    /**
     * 接收事件数据
     */
    @Override
    public void receiveEventData(ZabbixEventDTO zabbixEvent) {

        if (ObjectUtil.isNull(zabbixEvent)) {
            return;
        }
        List<TagsDTO> itemTags = zabbixEvent.getTags();

        // 获取Tags
        Map<String, String> tagMap = itemTags.stream().collect(Collectors.toMap(TagsDTO::getTag, TagsDTO::getValue));
        String deviceId = getDeviceId(zabbixEvent.getHosts());
        // 过滤Zabbix server不处理
        if (zabbixEvent.getGroups().contains("Zabbix servers")) {
            return;
        }
        if (tagMap.containsKey(IotConstants.DEVICE_STATUS_OFFLINE_TAG)) {
            // 设备离线
            deviceStatusService.offline(deviceId);
            log.debug("设备离线：{}", zabbixEvent);
        } else if (tagMap.containsKey(IotConstants.DEVICE_STATUS_ONLINE_TAG)) {
            // 设备上线
            deviceStatusService.online(deviceId, zabbixEvent.getClock());
            log.debug("设备上线：{}", zabbixEvent);
        } else if (tagMap.containsKey(IotConstants.ALARM_TAG_NAME)) {
            // 设备告警，发送到管理平台
            emqxClient.publish("server/device/" + deviceId + "/problem/" + zabbixEvent.getSeverity(), JsonUtils.toJsonString(zabbixEvent), 2);
            log.debug("设备告警：{}", zabbixEvent);
        } else {
            log.warn("未知事件：{}", zabbixEvent);
        }
    }

    /**
     * 获取设备id
     *
     * @param hosts 主机
     * @return {@link String}
     */
    private String getDeviceId(List<HostsDTO> hosts) {
        if (CollectionUtil.isEmpty(hosts)) {
            return null;
        }
        return hosts.get(0).getName();
    }

}
