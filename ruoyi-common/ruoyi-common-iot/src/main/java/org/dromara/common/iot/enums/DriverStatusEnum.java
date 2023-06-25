package org.dromara.common.iot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 驱动状态枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum DriverStatusEnum {
    ONLINE("ONLINE", "在线"),
    OFFLINE("OFFLINE", "离线"),
    MAINTAIN("MAINTAIN", "维护"),
    FAULT("FAULT", "故障"),
    ;


    /**
     * 状态编码
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
     * @return StatusEnum
     */
    public static DriverStatusEnum ofCode(String code) {
        Optional<DriverStatusEnum> any = Arrays.stream(DriverStatusEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return DriverStatusEnum
     */
    public static DriverStatusEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
