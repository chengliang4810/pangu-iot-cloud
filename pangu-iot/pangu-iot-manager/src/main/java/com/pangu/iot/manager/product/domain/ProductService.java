package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import com.pangu.iot.manager.device.enums.IotDataType;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Integer async;

//    @TableField(exist = false)
//    private Long relationId;

    /**
     * 数据类型
     */
    private IotDataType dataType;

    /**
     * 规格
     */
    private String specs;

}
