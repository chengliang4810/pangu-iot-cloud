package com.pangu.iot.manager.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 产品业务对象
 *
 * @author chengliang4810
 * @date 2022-12-30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ProductBO extends BaseEntity {

    /**
     * 产品主键
     */
    @NotBlank(message = "产品主键不能为空", groups = { EditGroup.class })
    private String productId;

    /**
     * 产品分组ID
     */
    @NotBlank(message = "产品分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String groupId;

    /**
     * 产品编号
     */
    @NotBlank(message = "产品编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = { AddGroup.class, EditGroup.class })
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
