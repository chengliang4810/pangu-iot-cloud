package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产品业务对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBO extends BaseEntity {

    /**
     * 产品主键
     */
    @NotNull(message = "产品主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 产品分组ID
     */
    @NotNull(message = "产品分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 产品编号
     */
    private String code;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 产品类型
     */
    @NotBlank(message = "产品类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 驱动
     */
    private List<String> driver;

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

}
