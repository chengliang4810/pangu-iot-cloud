package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 设备业务对象
 *
 * @author chengliang4810
 * @date 2023-01-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceBO extends BaseEntity {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 网关设备id
     */
    private Long gatewayDeviceId;

    /**
     * 设备编号
     */
    @Nullable
    @NotBlank
    @Size(min = 5, max = 60, groups = { AddGroup.class }, message = "设备ID长度不符合规范")
    @Pattern(regexp = "^\\w*$", message = "设备ID只能包含英文、数字、下划线", groups = { AddGroup.class })
    private String code;

    /**
     * 设备分组IDid
     */
    private Long groupId;

    /**
     * 设备分组ID
     */
    @Size(min = 1, max = 10, message = "设备分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private List<Long> groupIds;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;


    /**
     * 产品ID
     */
    private List<Long> productIds;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 设备地址
     */
    private String address;

    /**
     * 地址坐标
     */
    private String position;

    /**
     * 启用状态
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;

}
