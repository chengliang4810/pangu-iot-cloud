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
public class ZabbixEventDTO implements Serializable {

    /**
     * eventid
     */
    @JsonProperty("eventid")
    private Integer eventId;

    @JsonProperty("hosts")
    private List<HostsDTO> hosts;

    /**
     * 名字
     */
    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private Integer value;

    /**
     * 严重程度
     */
    @JsonProperty("severity")
    private Integer severity;

    /**
     * 时钟
     */
    @JsonProperty("clock")
    private Integer clock;
    /**
     * ns
     */
    @JsonProperty("ns")
    private Integer ns;

    /**
     * 组
     */
    @JsonProperty("groups")
    private List<String> groups;

    /**
     * 标签
     */
    @JsonProperty("tags")
    private List<TagsDTO> tags;

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
