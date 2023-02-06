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
 * 产品功能对象 iot_product_service
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_service")
public class ProductService extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 功能名称
     */
    private String name;
    /**
     * 功能标识
     */
    private String mark;
    /**
     * 备注
     */
    private String remark;
    /**
     * 执行方式 0-同步 1-异步
     */
    private Long async;

}
