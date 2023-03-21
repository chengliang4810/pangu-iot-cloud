package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备上下线规则与设备关系对象 iot_device_status_function_relation
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_status_function_relation")
public class DeviceStatusFunctionRelation extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 规则ID
     */
    private Long ruleId;
    /**
     * 产品/设备ID
     */
    private Long relationId;
    /**
     * zbx下线触发器主键
     */
    private String zbxId;
    /**
     * zbx上线触发器主键
     */
    private String zbxIdRecovery;

}
