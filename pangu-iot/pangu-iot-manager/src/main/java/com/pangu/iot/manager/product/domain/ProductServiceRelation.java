package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 产品功能关联关系对象 iot_product_service_relation
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_service_relation")
public class ProductServiceRelation extends BaseEntity {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
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
    private Boolean inherit;

    public ProductServiceRelation(Long serviceId, Long relationId){
        this.serviceId = serviceId;
        this.relationId = relationId;
    }

    public ProductServiceRelation(Long serviceId, Long relationId, Boolean inherit){
        this.serviceId = serviceId;
        this.relationId = relationId;
        this.inherit = inherit;
    }

}
