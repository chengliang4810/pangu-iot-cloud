package com.pangu.common.core.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class ZabbixItemDTO implements Serializable {

    @JsonProperty("itemid")
    private Integer itemId;

    @JsonProperty("host")
    private HostsDTO host;

    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private String value;

    @JsonProperty("type")
    private Integer type;

    /**
     * 组
     */
    @JsonProperty("groups")
    private List<String> groups;

    /**
     * 标签
     */
    @JsonProperty("item_tags")
    private List<TagsDTO> tags;

    /**
     * 秒
     */
    @JsonProperty("clock")
    private Integer clock;
    /**
     * 纳秒
     */
    @JsonProperty("ns")
    private Integer ns;


    public List<TagsDTO> getTags() {
        if (CollectionUtil.isNotEmpty(tags)){
            return tags;
        }
        return Collections.emptyList();
    }

    public List<String> getGroups() {
        if (CollectionUtil.isNotEmpty(tags)){
            return groups;
        }
        return Collections.emptyList();
    }

}
