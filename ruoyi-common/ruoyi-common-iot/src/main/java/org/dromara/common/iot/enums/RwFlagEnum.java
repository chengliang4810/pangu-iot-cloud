package org.dromara.common.iot.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用读写标识枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum RwFlagEnum {
    /**
     * 只读
     */
    R((byte) 0x00, "r", "只读"),

    /**
     * 只写
     */
    W((byte) 0x01, "w", "只写"),

    /**
     * 读写
     */
    RW((byte) 0x02, "rw", "读写"),
    ;

    /**
     * 索引
     */
    private final Byte index;

    /**
     * 编码
     */
    @EnumValue
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return RwFlagEnum
     */
    public static RwFlagEnum ofCode(String code) {
        Optional<RwFlagEnum> any = Arrays.stream(RwFlagEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return RwFlagEnum
     */
    public static RwFlagEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
