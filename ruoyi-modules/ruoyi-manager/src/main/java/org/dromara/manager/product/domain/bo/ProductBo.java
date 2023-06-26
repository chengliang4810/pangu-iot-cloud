package org.dromara.manager.product.domain.bo;

import org.dromara.manager.product.domain.Product;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 产品业务对象 iot_product
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Product.class, reverseConvertGenerate = false)
public class ProductBo extends BaseEntity {

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
     * 驱动ID
     */
    @NotNull(message = "驱动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long driverId;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 产品类型
     */
    @NotNull(message = "产品类型不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 描述
     */
    private String remark;


}
