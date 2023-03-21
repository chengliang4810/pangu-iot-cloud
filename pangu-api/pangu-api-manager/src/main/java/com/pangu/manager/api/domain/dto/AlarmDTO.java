package com.pangu.manager.api.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
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


}
