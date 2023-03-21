package com.pangu.iot.manager.product.domain.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class EnumTypeParams implements Serializable {

    /**
     * 枚举值
     */
    private List<EnumObj> enumList;

}
