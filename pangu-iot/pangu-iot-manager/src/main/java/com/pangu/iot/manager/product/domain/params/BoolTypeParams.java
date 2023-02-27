package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * bool类型参数
 * IotDataType = bool
 * @author chengliang
 * @date 2023/02/27
 */
@Data
@Accessors(chain = true)
public class BoolTypeParams implements Serializable {

    /**
     *  true对应的文本
     */
    private String trueText;

    /**
     * false对应的文字
     */
    private String falseText;


}
