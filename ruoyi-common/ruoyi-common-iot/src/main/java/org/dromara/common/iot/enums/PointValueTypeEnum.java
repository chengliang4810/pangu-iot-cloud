/*
 * Copyright 2016-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.common.iot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 通用位号数据类型枚举
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Getter
@AllArgsConstructor
public enum PointValueTypeEnum {
    BYTE("byte", "字节"),
    SHORT("short", "短整数"),
    INT("int", "整数"),
    LONG("long", "长整数"),
    FLOAT("float", "浮点数"),
    DOUBLE("double", "双精度浮点数"),
    BOOLEAN("boolean", "布尔量"),
    STRING("string", "字符串"),
    HEX("hex", "十六进制");

    /**
     * 位号数据类型编码
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
     * @return PointValueTypeEnum
     */
    public static PointValueTypeEnum ofCode(String code) {
        Optional<PointValueTypeEnum> any = Arrays.stream(PointValueTypeEnum.values()).filter(type -> type.getCode().equals(code)).findFirst();
        return any.orElse(null);
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return PointValueTypeEnum
     */
    public static PointValueTypeEnum ofName(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
