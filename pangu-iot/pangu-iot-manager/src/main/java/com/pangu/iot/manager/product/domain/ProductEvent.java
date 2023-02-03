package com.pangu.iot.manager.product.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 告警规则对象 iot_product_event
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_product_event")
public class ProductEvent extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 告警规则名称
     */
    private String name;
    /**
     * 告警等级
     */
    private Integer level;
    /**
     * and 或者 or
     */
    private String expLogic;
    /**
     * 0 否 1 是
     */
    private Integer notify;
    /**
     * 0 告警 1场景联动
     */
    private Integer classify;

    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 触发类型 0-条件触发 1-定时触发
     */
    private Integer triggerType;

    /**
     * 状态
     */
    private Boolean status;
    /**
     * 备注
     */
    private String remark;

}
