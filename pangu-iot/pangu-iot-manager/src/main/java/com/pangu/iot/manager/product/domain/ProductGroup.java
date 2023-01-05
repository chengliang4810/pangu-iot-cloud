package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.pangu.common.core.web.domain.TreeEntity;

/**
 * 产品分组对象 iot_product_group
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_group")
public class ProductGroup extends TreeEntity<ProductGroup> {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 所有父级ID
     */
    private String pids;
    /**
     * 备注
     */
    private String remark;

}
