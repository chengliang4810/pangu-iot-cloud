package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 数组类型参数
 * IotDataType = array
 * @author chengliang
 * @date 2023/02/27
 */
@Data
@Accessors(chain = true)
public class ArrayTypeParams implements Serializable {

    /**
     * 数组类型 integer  double string
     */
    private String arrayType;

}
