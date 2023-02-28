package com.pangu.manager.api;

import com.pangu.manager.api.domain.dto.AlarmDTO;

/**
 * 告警记录服务
 *
 * @author chengliang4810
 * @date 2023/02/13 14:52
 */
public interface RemoteAlarmService {


    /**
     * 添加报警记录
     *
     * @param alarmDTO 报警dto
     */
    void addAlarmRecord(AlarmDTO alarmDTO);

}
