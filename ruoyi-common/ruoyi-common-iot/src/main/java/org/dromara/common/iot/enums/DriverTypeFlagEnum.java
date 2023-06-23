package org.dromara.common.iot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用驱动类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DriverTypeFlagEnum {
    /**
     * 协议驱动
     */
    DRIVER((byte) 0x00, "driver", "协议类型驱动"),

    /**
     * 网关驱动
     */
    GATEWAY((byte) 0x01, "gateway", "网关类型驱动"),

    /**
     * 串联驱动
     */
    CONNECT((byte) 0x02, "connect", "串联类型驱动"),
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
     * @return DriverTypeFlagEnum
     */
    public static DriverTypeFlagEnum ofCode(String code) {
        Optional<DriverTypeFlagEnum> any = Arrays.stream(DriverTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return DriverTypeFlagEnum
     */
    public static DriverTypeFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
