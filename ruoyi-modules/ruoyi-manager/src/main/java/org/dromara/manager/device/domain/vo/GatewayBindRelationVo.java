package org.dromara.manager.device.domain.vo;

import org.dromara.manager.device.domain.GatewayBindRelation;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 网关设备绑定子设备关系视图对象 iot_gateway_bind_relation
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = GatewayBindRelation.class)
public class GatewayBindRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 网关设备ID
     */
    @ExcelProperty(value = "网关设备ID")
    private Long gatewayDeviceId;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;


}
