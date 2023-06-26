package org.dromara.manager.device.domain;

import org.dromara.common.mybatis.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 设备对象 iot_device
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device")
public class Device extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 设备分组ID
     */
    private Long groupId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备地址
     */
    private String address;

    /**
     * 地址坐标
     */
    private String position;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String remark;


}
