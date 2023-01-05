package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

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
    @NotNull(message = "设备编号不能为空", groups = { AddGroup.class, EditGroup.class })
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
    @NotBlank(message = "单位描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unit;

    /**
     * 主条目id

     */
    @NotBlank(message = "主条目id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String masterItemId;

    /**
     *  依赖属性 id， 当 source为18时不为空

     */
    @NotNull(message = " 依赖属性 id， 当 source为18时不为空不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long dependencyAttrId;

    /**
     * zabbix ItemId
     */
    @NotBlank(message = "zabbix ItemId不能为空", groups = { AddGroup.class, EditGroup.class })
    private String zbxId;

    /**
     * 继承的ID
     */
    @NotBlank(message = "继承的ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String templateId;

    /**
     * zabbix 值映射ID
     */
    @NotBlank(message = "zabbix 值映射ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String valueMapId;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
