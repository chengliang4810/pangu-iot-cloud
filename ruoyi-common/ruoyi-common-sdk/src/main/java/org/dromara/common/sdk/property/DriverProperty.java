package org.dromara.common.sdk.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.iot.entity.driver.DriverAttribute;
import org.dromara.common.iot.entity.point.PointAttribute;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 驱动配置文件 driver 字段内容
 *
 * @author pnoker, chengliang4810
 * @date 2023/06/24
 */
@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "driver")
public class DriverProperty {

    /**
     * 租户
     */
    private String tenant;

    /**
     * 驱动名称
     */
    private String name;

    /**
     * 驱动编号
     */
    private String code;

    /**
     * 描述
     */
    private String remark;

    /**
     * 定时任务相关属性
     */
    private ScheduleProperty schedule;

    /**
     * 驱动属性
     */
    private List<DriverAttribute> driverAttribute;

    /**
     * 位号属性
     */
    private List<PointAttribute> pointAttribute;

}
