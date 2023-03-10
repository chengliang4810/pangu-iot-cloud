package com.pangu.iot.data.zabbix;

import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.domain.dto.MqttMessageDTO;
import com.pangu.common.core.domain.dto.Tag;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.doamin.EmqxClient;
import com.pangu.common.zabbix.domain.DataMessage;
import com.pangu.common.zabbix.model.ZbxValue;
import com.pangu.common.zabbix.service.ReceiveDataService;
import com.pangu.common.zabbix.util.TimeUtil;
import com.pangu.iot.data.service.DeviceStatusService;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.SUPER_TABLE_PREFIX;

/**
 * ZBX数据消费者服务
 *
 * @author chengliang4810
 * @date 2023/01/11 12:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataConsumerService implements ReceiveDataService {

    private final TdEngineService tdEngineService;
    private final DeviceStatusService deviceStatusService;
    private final EmqxClient emqxClient;

    /**
     * 接收ZBX数据，处理
     *
     * @param dataMessage ZBX数据
     */
    @Override
    public void receiveData(DataMessage dataMessage) {
        dataMessage.ack();
        ZbxValue zbxValue = dataMessage.getData();
        // 过滤Zabbix server不处理
        if (zbxValue.getHostname().equals("Zabbix server")){
            return;
        }
        log.debug("接收到ZBX数据：{}", zbxValue);
        Map<String, String> tagMap = zbxValue.getItemTags().stream().collect(Collectors.toMap(Tag::getTag, Tag::getValue));
        if (!tagMap.containsKey(IotConstants.PRODUCT_ID_TAG_NAME)){
            log.warn("未知产品：{}", zbxValue);
            return;
        }
        // 存入tdengine
        // 产品Id
        String productId = tagMap.get(IotConstants.PRODUCT_ID_TAG_NAME);
        String attributeKey = tagMap.get(IotConstants.ATTRIBUTE_KEY_TAG_NAME);
        // 获取当前设备状态
        boolean status = deviceStatusService.getOnlineStatus(zbxValue.getHostname());

        Map<String, Object> value = new LinkedHashMap<>(3);
        value.put(IotConstants.TABLE_STATUS_FIELD, status ? 1 : 0);
        value.put(attributeKey, zbxValue.getValue());
        value.put(IotConstants.TABLE_PRIMARY_FIELD, TimeUtil.toTimestamp(zbxValue.getClock(), zbxValue.getNs()));
        tdEngineService.insertData(IotConstants.DEVICE_TABLE_NAME_PREFIX + zbxValue.getHostname(),SUPER_TABLE_PREFIX + productId, value);
        // 发送到EMQX
        MqttMessageDTO mqttMessageDTO = new MqttMessageDTO(zbxValue.getHostname(), zbxValue.getName(), zbxValue.getValue(), zbxValue.getClock());
        emqxClient.publish("iot/device/" + zbxValue.getHostname() + "/attribute/" + zbxValue.getName(), JsonUtils.toJsonStringBytes(mqttMessageDTO));
    }

}
