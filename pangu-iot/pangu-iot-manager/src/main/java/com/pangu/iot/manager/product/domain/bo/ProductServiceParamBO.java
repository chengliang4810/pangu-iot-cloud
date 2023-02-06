package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.pangu.common.core.web.domain.BaseEntity;

/**
 * 产品功能参数业务对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductServiceParamBO extends BaseEntity {

    /**
     * 
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 服务ID
     */
    @NotNull(message = "服务ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long serviceId;

    /**
     * 参数标识
     */
    @NotBlank(message = "参数标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String key;

    /**
     * 参数名
     */
    @NotBlank(message = "参数名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 参数值
     */
    @NotBlank(message = "参数值不能为空", groups = { AddGroup.class, EditGroup.class })
    private String value;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 设备IDremark
     */
    @NotNull(message = "设备IDremark不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;


}
