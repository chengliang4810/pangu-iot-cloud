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
 * 产品对象 iot_product
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID=1L;

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
     * 产品编号
     */
    private String code;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品类型
     */
    private String type;
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
     * 备注
     */
    private String remark;
    /**
     * Zabbix对应模板ID

     */
    private String zbxId;

}
