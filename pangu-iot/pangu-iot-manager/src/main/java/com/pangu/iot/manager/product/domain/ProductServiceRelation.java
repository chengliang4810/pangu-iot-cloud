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
 * 产品功能关联关系对象 iot_product_service_relation
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_service_relation")
public class ProductServiceRelation extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 服务ID
     */
    private Long serviceId;
    /**
     * 关联ID
     */
    private Long relationId;
    /**
     * 是否继承
     */
    private Long inherit;

}
