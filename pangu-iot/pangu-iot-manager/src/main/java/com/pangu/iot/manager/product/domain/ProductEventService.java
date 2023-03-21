package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 告警规则与功能关系对象 iot_product_event_service
 *
 * @author chengliang4810
 * @date 2023-02-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_event_service")
public class ProductEventService extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 告警规则ID
     */
    private Long eventRuleId;
    /**
     * 功能ID
     */
    private Long serviceId;
    /**
     * 产品、设备ID
     */
    private Long relationId;
    /**
     * 执行目标设备ID
     */
    private Long executeDeviceId;

}
