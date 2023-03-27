package com.pangu.common.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TagsDTO implements Serializable {

    /**
     * 标签
     */
    @JsonProperty("tag")
    private String tag;

    /**
     * 值
     */
    @JsonProperty("value")
    private String value;

}
