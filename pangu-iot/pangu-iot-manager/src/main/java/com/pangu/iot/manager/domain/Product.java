package com.pangu.iot.manager.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品对象 iot_product
 *
 * @author chengliang4810
 * @date 2022-12-30
 */
@Data
@TableName("iot_product")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 产品主键
     */
    @TableId(value = "product_id")
    private String productId;

    /**
     * 产品分组ID
     */
    private String groupId;

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
