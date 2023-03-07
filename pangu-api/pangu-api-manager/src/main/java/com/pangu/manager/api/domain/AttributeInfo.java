package com.pangu.manager.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 属性配置
 *
 * @author chengliang
 * @date 2023/03/06
 */
@Data
@AllArgsConstructor
public class AttributeInfo {
    /**
     * 值，string，需要通过type确定真实的数据类型
     */
    private String value;

    /**
     * 类型，value type，用于确定value的真实类型
     */
    private String type;
}
