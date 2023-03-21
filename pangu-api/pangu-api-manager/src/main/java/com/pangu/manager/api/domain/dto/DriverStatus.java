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
     * 每个驱动唯一标识
     */
    private String primaryKey;

    /**
     * 状态
     */
    private OnlineStatus status;

    public DriverStatus(String primaryKey, OnlineStatus online) {
        this.primaryKey = primaryKey;
        this.status = online;
    }

}
