package com.pangu.iot.manager.alarm.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 告警记录对象 iot_problem
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_problem")
public class Problem extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * event_id
     */
    @TableId(value = "event_id")
    private Long eventId;
    /**
     * 对象ID
     */
    private Long objectId;
    /**
     * 告警级别
     */
    private Integer severity;
    /**
     * 名称
     */
    private String name;
    /**
     * 待确认状态
     */
    private Long acknowledged;
    /**
     * 时间
     */
    private LocalDateTime clock;
    /**
     * 解决时间
     */
    private LocalDateTime rClock;
    /**
     * 设备ID
     */
    private Long deviceId;

}
