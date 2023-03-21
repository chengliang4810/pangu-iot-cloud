package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 数值类型参数
 * IotDataType = integer
 * IotDataType = decimal
 * @author chengliang
 * @date 2023/02/27
 */
@Data
@Accessors(chain = true)
public class NumberTypeParams implements Serializable {

    /**
     * 最小值
     */
    private Integer min;

    /**
     * 最大值
     */
    private Integer max;

    /**
     * 步长
     */
    private Integer step;

    /**
     * 单位
     */
    private Integer unit;

}

