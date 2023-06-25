package org.dromara.common.iot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用使能标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum EnableFlagEnum {
    /**
     * 禁用
     */
    DISABLE((byte) 0x00, "disable", "禁用"),

    /**
     * 启用
     */
    ENABLE((byte) 0x01, "enable", "启用"),

    /**
     * 暂存
     */
    TEMP((byte) 0x02, "temp", "暂存"),
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
     * @return EnableFlagEnum
     */
    public static EnableFlagEnum ofCode(String code) {
        Optional<EnableFlagEnum> any = Arrays.stream(EnableFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return EnableFlagEnum
     */
    public static EnableFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
