package org.dromara.manager.device.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.device.domain.DeviceAttribute;

import java.util.Map;

/**
 * 设备属性业务对象 iot_device_attribute
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DeviceAttribute.class, reverseConvertGenerate = false)
public class DeviceAttributeBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 产品ID
     */
    @NotNull(message = "产品编号不能为空", groups = { AddGroup.class })
    @Null(message = "参数异常", groups = { EditGroup.class })
    private Long productId;

    /**
     * 设备编号
     */
    @Null(message = "参数异常", groups = { EditGroup.class })
    private Long deviceId;

    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attributeName;

    /**
     * 标识符
     */
    @NotBlank(message = "标识符不能为空", groups = { AddGroup.class, EditGroup.class })
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*$", message = "标识符只能包含字母、数字、下划线, 且不能数字开头", groups = { AddGroup.class, EditGroup.class })
    private String identifier;

    /**
     * 属性类型
     */
    @NotBlank(message = "属性类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attributeType;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数据预处理代码
     */
    private String pretreatmentScript;

    /**
     * 值映射ID
     */
    private Long valueMapId;

    /**
     * 描述
     */
    private String remark;

    /**
     * 属性点位配置信息
     * key: 属性ID
     * value: 属性值
     */
    private Map<Long, String> pointAttributeConfig;

}
