package org.dromara.common.iot.constant;

/**
 * 任务调度 相关常量
 *
 * @author pnoker
 * @since 2022.1.0
 */
public class ScheduleConstant {

    private ScheduleConstant() {}

    /**
     * 驱动任务调度分组
     */
    public static final String DRIVER_SCHEDULE_GROUP = "DriverScheduleGroup";

    /**
     * 读任务
     */
    public static final String READ_SCHEDULE_JOB = "ReadScheduleJob";

    /**
     * 自定义任务
     */
    public static final String CUSTOM_SCHEDULE_JOB = "CustomScheduleJob";

    /**
     * 状态任务
     */
    public static final String STATUS_SCHEDULE_JOB = "StatusScheduleJob";

    /**
     * 网关状态任务
     */
    public static final String GATEWAY_STATUS_SCHEDULE_JOB = "GatewayStatusScheduleJob";

    /**
     * 驱动状态任务 Corn
     */
    public static final String DRIVER_STATUS_CORN = "0/5 * * * * ?";

}
