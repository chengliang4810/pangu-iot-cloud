package org.dromara.manager.product.domain;

import lombok.experimental.Accessors;
import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 产品对象 iot_product
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product")
public class Product extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 产品主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 产品分组ID
     */
    private Long groupId;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品类型
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 厂家
     */
    private String manufacturer;

    /**
     * 型号
     */
    private String model;

    /**
     * 设备总数
     */
    private Long deviceCount;

    /**
     * 描述
     */
    private String remark;


}
