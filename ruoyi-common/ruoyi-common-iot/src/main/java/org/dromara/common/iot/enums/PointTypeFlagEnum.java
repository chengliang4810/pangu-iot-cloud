package org.dromara.common.iot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用位号类型标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum PointTypeFlagEnum {
    /**
     * 字符串
     */
    STRING((byte) 0x00, "string", "字符串"),

    /**
     * 字节
     */
    BYTE((byte) 0x01, "byte", "字节"),

    /**
     * 短整数
     */
    SHORT((byte) 0x02, "short", "短整数"),

    /**
     * 整数
     */
    INT((byte) 0x03, "int", "整数"),

    /**
     * 长整数
     */
    LONG((byte) 0x04, "long", "长整数"),

    /**
     * 浮点数
     */
    FLOAT((byte) 0x05, "float", "浮点数"),

    /**
     * 双精度浮点数
     */
    DOUBLE((byte) 0x06, "double", "双精度浮点数"),

    /**
     * 布尔量
     */
    BOOLEAN((byte) 0x07, "boolean", "布尔量"),
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
     * @return PointTypeFlagEnum
     */
    public static PointTypeFlagEnum ofCode(String code) {
        Optional<PointTypeFlagEnum> any = Arrays.stream(PointTypeFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return PointTypeFlagEnum
     */
    public static PointTypeFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
