package com.pangu.common.sdk.bean.schedule;

import lombok.Getter;
import lombok.Setter;

/**
 * 驱动配置文件 driver.schedule 字段内容
 *
 * @author chengliang
 * @date 2023/02/28
 */
@Setter
@Getter
public class ScheduleProperty {
    private String read;
    private String heartbeat;
}
