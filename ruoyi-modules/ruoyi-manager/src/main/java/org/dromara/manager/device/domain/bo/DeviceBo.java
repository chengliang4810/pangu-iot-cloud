package org.dromara.manager.device.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.device.domain.Device;

import java.util.Map;

/**
 * 设备业务对象 iot_device
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Device.class, reverseConvertGenerate = false)
public class DeviceBo extends BaseEntity {

    /**
     * 设备主键
     */
    @NotNull(message = "设备主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 设备分组ID
     */
    @NotNull(message = "设备分组ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long groupId;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空", groups = { AddGroup.class })
    private Long productId;

    /**
     * 设备编号
     * 字母、数字和下划线组成，且不能以数字开头
     */
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "设备编号格式不正确", groups = { AddGroup.class, EditGroup.class })
    private String code;

    /**
     * 设备名称
     */
    @NotBlank(message = "设备名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 设备类型
     */
    private Integer deviceType;

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
    @NotNull(message = "启用状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer status;

    /**
     * 描述
     */
    private String remark;

    /**
     * 网关设备驱动配置信息
     * key: 属性ID
     * value: 属性值
     */
    private Map<Long, String> driverAttributeConfig;

}
