package org.dromara.manager.driver.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.manager.driver.domain.Driver;

/**
 * 驱动业务对象 iot_driver
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = Driver.class, reverseConvertGenerate = false)
public class DriverBo extends BaseEntity {

    /**
     * 主键ID
     */
    @NotNull(message = "主键ID不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 协议唯一性标识
     */
    @NotBlank(message = "协议唯一性标识不能为空", groups = {AddGroup.class, EditGroup.class})
    private String code;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String displayName;

    /**
     * 启用|禁用
     */
    @NotNull(message = "启用|禁用不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long enable;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;


}
