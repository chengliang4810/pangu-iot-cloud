package com.pangu.common.sdk.context;

import com.pangu.manager.api.enums.OnlineStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class DriverContext {

    /**
     * 驱动 状态，默认为 未注册 状态
     */
    private OnlineStatus driverStatus = OnlineStatus.OFFLINE;

    public synchronized void setDriverStatus(OnlineStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

}
