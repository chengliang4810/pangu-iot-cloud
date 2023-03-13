package com.pangu.common.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * mqtt消息类型
 *
 * @author chengliang
 * @date 2023/03/13
 */
@Getter
public enum MqttMessageType {

    DATA("data", "实时数据"),
    ALARM("alarm", "告警数据"),
    EVENT("event", "事件数据"),
    COMMAND("command", "命令数据"),
    ONLINE("online", "设备上线"),
    OFFLINE("offline", "设备下线");

    /**
     * 代码
     */
    @JsonValue
    private final String code;

    /**
     * 描述
     */
    private final String desc;

    MqttMessageType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
