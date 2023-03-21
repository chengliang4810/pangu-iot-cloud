package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * enum 对象
 *
 * @author chengliang
 * @date 2023/02/27
 */
@Data
@Accessors(chain = true)
public class EnumObj implements Serializable {

    /**
     * 文本
     */
    private String text;
    /**
     * 值
     */
    private String value;

}
