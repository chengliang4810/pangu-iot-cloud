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
public enum DeviceCommandTypeEnum {
    /**
     * 读位号值类型指令
     */
    READ((byte) 0x00, "read", "读位号值类型指令"),

    /**
     * 写位号值类型指令
     */
    WRITE((byte) 0x01, "write", "写位号值类型指令"),

    /**
     * 配置设备类型指令
     */
    CONFIG((byte) 0x02, "config", "配置设备类型指令"),
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
     * @return DeviceCommandTypeEnum
     */
    public static DeviceCommandTypeEnum ofCode(String code) {
        Optional<DeviceCommandTypeEnum> any = Arrays.stream(DeviceCommandTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return DeviceCommandTypeEnum
     */
    public static DeviceCommandTypeEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
