package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 字符串类型参数
 * IotDataType = string
 * @author chengliang
 * @date 2023/02/27
 */
@Data
@Accessors(chain = true)
public class StringTypeParams implements Serializable {

    /**
     * 最大长度
     */
    private Integer maxLength;

}
