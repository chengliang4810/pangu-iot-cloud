package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 设备业务对象
 *
 * @author chengliang4810
 * @date 2023-01-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceBO extends BaseEntity {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备编号
     */
    @NotBlank(message = "设备编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 设备分组ID
     */
    @NotNull(message = "设备分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 设备类型
     */
    @NotBlank(message = "设备类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 设备地址
     */
    @NotBlank(message = "设备地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String address;

    /**
     * 地址坐标
     */
    @NotBlank(message = "地址坐标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String position;

    /**
     * 最近在线时间
     */
    @NotNull(message = "最近在线时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date latestOnline;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * Zabbix对应模板ID
     */
    @NotBlank(message = "Zabbix对应模板ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String zbxId;


}
