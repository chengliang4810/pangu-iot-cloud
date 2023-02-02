package com.pangu.common.zabbix.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 告警问题模型
 *
 * @author chengliang4810
 * @date 2023/01/11 14:34
 */
@Data
@Accessors(chain = true)
public class ZbxProblem implements Serializable {

    /**
     *  zabbix事件id
     */
    private Integer eventid;
    /**
     * 主机名
     */
    private String hostname;
    /**
     * 报警级别
     */
    private Integer severity;
    /**
     * 事件名称
     */
    private String name;
    /**
     * 时间
     */
    private Integer clock;

    /**
     * 分组
     */
    private List<String> groups;

    /**
     * 标签
     */
    @JsonAlias("tags")
    private List<ZbxTag> itemTags;

}
