package com.pangu.common.core.domain.dto;

import com.pangu.common.core.enums.MqttMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * mqtt消息
 * 在这里定义mqtt 实时数据与问题消息的格式
 * @author chengliang
 * @date 2023/03/13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MqttMessageDTO {

    /**
     * 消息类型
     */
    private MqttMessageType type = MqttMessageType.DATA;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 属性
     */
    private String attribute;

    /**
     * 值
     */
    private String value;

    /**
     * 告警级别
     */
    private Integer level;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 时间
     */
    private Integer clock;


    /**
     * mqtt消息dto
     *
     * @param deviceId  设备id
     * @param attribute 属性
     * @param value     价值
     * @param clock     时钟
     */
    public MqttMessageDTO(String deviceId, String attribute, String value, Integer clock) {
        this.deviceId = deviceId;
        this.attribute = attribute;
        this.value = value;
        this.clock = clock;
    }


    /**
     * mqtt消息dto
     *
     * @param type      类型
     * @param deviceId  设备id
     * @param level     水平
     * @param eventName 事件名称
     */
    public MqttMessageDTO(MqttMessageType type, String deviceId, Integer level, String eventName) {
        this.type = type;
        this.deviceId = deviceId;
        this.level = level;
        this.eventName = eventName;
    }

    /**
     * mqtt消息dto
     *
     * @param offline  离线
     * @param deviceId 设备id
     */
    public MqttMessageDTO(MqttMessageType offline, String deviceId, String deviceName) {
        this.type = offline;
        this.deviceId = deviceId;
        this.eventName = offline.getDesc();
        this.value = deviceName;
    }

}
