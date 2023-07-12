package org.dromara.common.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用属性类型标识枚举
 *
 * @author pnoker, chengliang4810
 * @date 2023/07/12
 */
@Getter
@AllArgsConstructor
public enum AttributeType {

    /**
     * 字符串
     */
    STRING(0, "string", "字符串"),

    /**
     * 字节
     */
    BYTE(1, "byte", "字节"),

    /**
     * 短整数
     */
    SHORT(2, "short", "短整数"),

    /**
     * 整数
     */
    INT(3, "int", "整数"),

    /**
     * 长整数
     */
    LONG(4, "long", "长整数"),

    /**
     * 浮点数
     */
    FLOAT(5, "float", "浮点数"),

    /**
     * 双精度浮点数
     */
    DOUBLE(6, "double", "双精度浮点数"),

    /**
     * 布尔量
     */
    BOOLEAN(7, "boolean", "布尔量"),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Integer index;

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
     * @return AttributeTypeFlagEnum
     */
    public static AttributeType ofCode(String code) {
        Optional<AttributeType> any = Arrays.stream(AttributeType.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return AttributeTypeFlagEnum
     */
    public static AttributeType ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
