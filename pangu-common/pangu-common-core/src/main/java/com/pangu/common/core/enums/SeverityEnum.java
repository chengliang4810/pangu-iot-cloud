package com.pangu.common.core.enums;

import lombok.Getter;

/**
 * 严重级别枚举
 *
 * @author chengliang4810
 * @date 2023/02/03 16:23
 */
@Getter
public enum SeverityEnum {

    INFO("信息", "1"),
    WARN("低级", "2"),
    ALARM("中级", "3"),
    HIGH("高级", "4"),
    DISASTER("紧急", "5");

    String code;
    String describe;

    SeverityEnum(String describe, String code) {
        this.code = code;
        this.describe = describe;
    }

    public static String getDescribe(String code) {
        if (code == null) {
            return "";
        } else {
            for (SeverityEnum s : SeverityEnum.values()) {
                if (s.getCode().equals(code)) {
                    return s.getDescribe();
                }
            }
            return "未分类";
        }
    }
}
