package com.pangu.common.core.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 属性配置
 *
 * @author chengliang
 * @date 2023/03/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeInfo implements Serializable {
    /**
     * 值，string，需要通过type确定真实的数据类型
     */
    private Object value;

    /**
     * 类型，value type，用于确定value的真实类型
     */
    private String type;
}
