package org.dromara.manager.thing.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 物模型功能对象
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_thing_function")
public class ThingFunction extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 驱动ID
     */
    private Long driverId;

    /**
     * 物模型属性
     */
    private Long functionStatusAttribute;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据类型对象参数
     */
    private String specs;

    /**
     * 执行方式
     */
    private Long async;

    /**
     * 描述
     */
    private String remark;


}
