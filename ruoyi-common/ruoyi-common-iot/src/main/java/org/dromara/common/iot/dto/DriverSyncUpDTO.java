package org.dromara.common.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.core.constant.SymbolConstants;
import org.dromara.common.iot.entity.driver.DriverAttribute;
import org.dromara.common.iot.entity.point.PointAttribute;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSyncUpDTO {

    private String tenant;
    private DriverDTO driver;
    private List<DriverAttribute> driverAttributes;
    private List<PointAttribute> pointAttributes;

    /**
     * 获取驱动唯一键
     *
     * @return {@link String}
     */
    public String getDriverUniqueKey(){
        return driver.getServiceName() + SymbolConstants.UNDERSCORE + driver.getServiceHost() + SymbolConstants.UNDERSCORE + driver.getServicePort();
    }

}
