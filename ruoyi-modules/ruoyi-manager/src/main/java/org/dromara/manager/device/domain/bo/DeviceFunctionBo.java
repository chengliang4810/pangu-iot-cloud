package org.dromara.manager.device.domain.bo;

import org.dromara.manager.device.domain.DeviceFunction;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 设备功能业务对象 iot_device_function
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceFunction.class, reverseConvertGenerate = false)
public class DeviceFunctionBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 产品ID
     */
    @NotNull(message = "产品ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long productId;

    /**
     * 设备编号
     */
    @NotNull(message = "设备编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;

    /**
     * 驱动ID
     */
    @NotNull(message = "驱动ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long driverId;

    /**
     * 设备属性
     */
    @NotNull(message = "设备属性不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long functionStatusAttribute;

    /**
     * 功能名称
     */
    @NotBlank(message = "功能名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String functionName;

    /**
     * 标识符
     */
    @NotBlank(message = "标识符不能为空", groups = { AddGroup.class, EditGroup.class })
    private String identifier;

    /**
     * 数据类型
     */
    @NotBlank(message = "数据类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dataType;

    /**
     * 数据类型对象参数
     */
    @NotBlank(message = "数据类型对象参数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specs;

    /**
     * 执行方式
     */
    @NotNull(message = "执行方式不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long async;

    /**
     * 描述
     */
    private String remark;


}
