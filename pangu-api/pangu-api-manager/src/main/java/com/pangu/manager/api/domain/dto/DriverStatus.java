package com.pangu.manager.api.domain.dto;

import com.pangu.manager.api.enums.OnlineStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DriverStatus implements Serializable {

    /**
     * 唯一驱动名称
     */
    private String primaryDriverName;

    /**
     * 状态
     */
    private OnlineStatus status;

    public DriverStatus(String primaryServiceName, OnlineStatus online) {
        this.primaryDriverName = primaryServiceName;
        this.status = online;
    }

}
