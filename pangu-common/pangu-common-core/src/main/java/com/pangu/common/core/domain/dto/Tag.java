package com.pangu.common.core.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Tag {

    /**
     * 标签名
     */
    private String tag;

    /**
     * 值
     */
    private String value;

}
