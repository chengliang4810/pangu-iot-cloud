package com.pangu.common.core.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HostsDTO {

    @JsonProperty("host")
    private String host;
    @JsonProperty("name")
    private String name;

}
