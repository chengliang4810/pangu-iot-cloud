package com.pangu.common.core.enums;

import lombok.Getter;

/**
 * 常见时间单位
 *
 * @author chengliang4810
 * @date 2023/02/03 16:22
 */
public enum CommonTimeUnit {

    S("s", "秒"),
    M("m", "分"),
    H("h", "小时"),
    D("d", "天");

    @Getter
    String code;
    @Getter
    String message;

    CommonTimeUnit(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getDescription(String status) {
        if (status == null) {
            return "";
        } else {
            for (CommonTimeUnit s : CommonTimeUnit.values()) {
                if (s.getCode().equals(status)) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
