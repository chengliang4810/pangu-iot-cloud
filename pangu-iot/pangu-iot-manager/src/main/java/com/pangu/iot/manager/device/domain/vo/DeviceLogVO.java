package com.pangu.iot.manager.device.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备日志
 *
 * @author chengliang4810
 * @date 2023/02/14 10:11
 */
@Data
@Builder
@Accessors(chain = true)
public class DeviceLogVO {


    private String logType;

    private String triggerTime;

    private String content;

    // @CachedValue(type = DicType.Device, fieldName = "deviceName")
    private String deviceId;

    private String deviceName;

    // @CachedValue(value = "EVENT_LEVEL", fieldName = "severityName")
    private String severity;

    private String severityName;

    private String param;

    private String status;

    private String triggerType;

    private String triggerBody;

    private String key;

    private Long eventRuleId;

    private Long userId;

    // private List<DeviceRelationDto> triggerDevice;

    // private List<DeviceRelationDto> executeDevice;

}
