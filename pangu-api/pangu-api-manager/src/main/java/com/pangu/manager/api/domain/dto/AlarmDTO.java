package com.pangu.manager.api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AlarmDTO implements Serializable {

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


    public AlarmDTO(Long eventRuleId, Integer severity, Long deviceId, long eventId, long acknowledged, LocalDateTime clock) {
        this.objectId = eventRuleId;
        this.severity = severity;
        this.deviceId = deviceId;
        this.eventId = eventId;
        this.acknowledged = acknowledged;
        this.clock = clock;
    }

}
