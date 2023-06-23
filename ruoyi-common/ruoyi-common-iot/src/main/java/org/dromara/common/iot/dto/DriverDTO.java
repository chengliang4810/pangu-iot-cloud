package org.dromara.common.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO implements Serializable {

    /**
     * 驱动名字
     */
    private String driverName;

    /**
     * 驱动代码
     */
    private String driverCode;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务主机
     */
    private String serviceHost;

    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 备注
     */
    private String remark;

}
