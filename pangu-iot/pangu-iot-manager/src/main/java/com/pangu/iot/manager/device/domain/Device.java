package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备对象 iot_device
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device")
public class Device extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 设备主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 设备编号
     */
    private String code;
    /**
     * 设备分组ID
     */
    private Long groupId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 设备类型
     */
    private String type;
    /**
     * 设备地址
     */
    private String address;
    /**
     * 地址坐标
     */
    private String position;
    /**
     * 最近在线时间
     */
    private Date latestOnline;
    /**
     * 备注
     */
    private String remark;
    /**
     * Zabbix对应模板ID
     */
    private String zbxId;

}
