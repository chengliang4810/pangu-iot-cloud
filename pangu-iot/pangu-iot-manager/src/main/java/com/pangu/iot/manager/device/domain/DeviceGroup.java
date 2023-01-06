package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备分组对象 iot_device_group
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_group")
public class DeviceGroup extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 分组名称
     */
    private String name;
    /**
     * zabbix ItemId
     */
    private String zbxId;
    /**
     * 备注
     */
    private String remark;

}
