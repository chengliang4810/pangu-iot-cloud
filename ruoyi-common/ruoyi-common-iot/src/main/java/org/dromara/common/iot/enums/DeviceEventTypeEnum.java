package org.dromara.common.iot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用驱动事件枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DeviceEventTypeEnum {
    /**
     * 心跳事件
     */
    HEARTBEAT((byte) 0x00, "heartbeat", "心跳事件"),

    /**
     * 报警事件
     */
    ALARM((byte) 0x01, "alarm", "报警事件"),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Byte index;

    /**
     * 编码
     */
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return DeviceEventTypeEnum
     */
    public static DeviceEventTypeEnum ofCode(String code) {
        Optional<DeviceEventTypeEnum> any = Arrays.stream(DeviceEventTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return DeviceEventTypeEnum
     */
    public static DeviceEventTypeEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
