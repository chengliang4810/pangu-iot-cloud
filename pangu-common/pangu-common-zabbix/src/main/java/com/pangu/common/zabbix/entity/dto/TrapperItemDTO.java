package com.pangu.common.zabbix.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * 监控项dto
 *
 * @author chengliang
 * @date 2022/11/02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TrapperItemDTO {

    /**
     * item id
     */
    private String itemId;
    /**
     * 主机id 对应产品/设备的Zbx主键
     */
    private String hostId;
    /**
     * 监控项名称 例如：温度
     */
    private String itemName;
    /**
     * 监控项关键词 例如 temp
     */
    private String key;
    /**
     * 数据来源:
     * 主动上报 2
     * 根据单个属性预处理 18
     * zabbix Agent 采集 0
     */
    private String source;

    /**
     * 值类型:
     * 浮点数 0
     * 整数 3
     * 字符 1
     * 文本 4
     */
    private String valueType;

    /**
     * 单位 例如 摄氏度
     */
    private String units;

    /**
     * 依赖的属性ID
     * 当source == 18 时为依赖其他属性，则该属性不能为空。
     */
    private String dependencyItemZbxId;

    /**
     * 值映射zabbix ID
     */
    private String valueMapId;

    /**
     * 标签
     */
    private Map<String, String> tags;

}
