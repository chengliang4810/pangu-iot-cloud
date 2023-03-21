package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品功能参数对象 iot_product_service_param
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_service_param")
public class ProductServiceParam extends BaseEntity {

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
     * 参数标识
     */
    @TableField(value = "`key`")
    private String key;
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数值
     */
    private String value;
    /**
     * 备注
     */
    private String remark;
    /**
     * 设备IDremark
     */
    private Long deviceId;

}
