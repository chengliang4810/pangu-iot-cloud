package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 设备属性业务对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceAttributeBO extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 设备编号
     */
    private Long deviceId;

    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 属性唯一Key

     */
    @NotBlank(message = "属性唯一Key不能为空", groups = { AddGroup.class, EditGroup.class })
    private String key;


    /**
     * 是否返回最新数据
     */
    private Boolean latestData;

    /**
     * 值类型
     */
    @NotBlank(message = "值类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String valueType;

    /**
     * 来源
     */
    @NotBlank(message = "来源不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * zabbix 值映射ID
     */
    private String valueMapId;

    /**
     * 备注
     */
    private String remark;


}
