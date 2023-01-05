package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 设备属性对象 iot_device_attribute
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_attribute")
public class DeviceAttribute extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 设备编号
     */
    private Long deviceId;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性唯一Key
     */
    @TableField("`key`")
    private String key;
    /**
     * 值类型
     */
    private String valueType;
    /**
     * 来源
     */
    private String source;
    /**
     * 单位描述
     */
    private String unit;
    /**
     * 主条目id
     */
    private String masterItemId;
    /**
     *  依赖属性 id， 当 source为18时不为空
     */
    private Long dependencyAttrId;
    /**
     * zabbix ItemId
     */
    private String zbxId;
    /**
     * 继承的ID
     */
    private String templateId;
    /**
     * zabbix 值映射ID
     */
    private String valueMapId;
    /**
     * 备注
     */
    private String remark;

}
