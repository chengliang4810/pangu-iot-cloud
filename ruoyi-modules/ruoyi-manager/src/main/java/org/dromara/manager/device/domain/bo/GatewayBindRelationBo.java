package org.dromara.manager.device.domain.bo;

import org.dromara.manager.device.domain.GatewayBindRelation;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 网关设备绑定子设备关系业务对象 iot_gateway_bind_relation
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = GatewayBindRelation.class, reverseConvertGenerate = false)
public class GatewayBindRelationBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 网关设备ID
     */
    @NotNull(message = "网关设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long gatewayDeviceId;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;


}
