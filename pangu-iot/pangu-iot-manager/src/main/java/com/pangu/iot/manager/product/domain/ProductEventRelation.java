package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 告警规则关系对象 iot_product_event_relation
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_event_relation")
public class ProductEventRelation extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 规则ID
     */
    private Long eventRuleId;
    /**
     * 关联产品或设备ID
     */
    private Long relationId;
    /**
     * trigger id
     */
    private String zbxId;
    /**
     * 是否来自产品
     */
    private Long inherit;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 备注
     */
    private String remark;

    public ProductEventRelation(Long eventRuleId, Long productId,String zbxId, String remark) {
        this.eventRuleId = eventRuleId;
        this.relationId = productId;
        this.zbxId = zbxId;
        this.remark = remark;
    }

}
