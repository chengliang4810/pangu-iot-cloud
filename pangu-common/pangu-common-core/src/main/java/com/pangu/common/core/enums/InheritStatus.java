package com.pangu.common.core.enums;

import lombok.Getter;

/**
 * 是否来自产品 状态
 *
 * @author chengliang4810
 * @date 2023/02/03 16:23
 */
public enum InheritStatus {

    NO("0", "否"),
    YES("1", "是");

    @Getter
    String code;
    @Getter
    String message;

    InheritStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getDescription(String status) {
        if (status == null) {
            return "";
        } else {
            for (InheritStatus s : InheritStatus.values()) {
                if (s.getCode().equals(status)) {
                    return s.getMessage();
                }
            }
            return "";
        }
    }
}
