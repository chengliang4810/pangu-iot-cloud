package org.dromara.common.sdk.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 驱动配置文件 driver.schedule 字段内容
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
public class ScheduleProperty {

    /**
     * 读任务配置
     */
    private ScheduleConfig read;

    /**
     * 自定义任务配置
     */
    private ScheduleConfig custom;

    /**
     * 网关状态任务配置
     */
    private ScheduleConfig gateway;

    /**
     * 驱动调度任务配置
     *
     * @author pnoker
     * @since 2022.1.0
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleConfig {
        private Boolean enable = false;
        private String corn = "* */15 * * * ?";
    }
}
