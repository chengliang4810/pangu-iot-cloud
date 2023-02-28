package com.pangu.manager.api.domain.dto;

import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.manager.api.domain.PointAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DriverDTO implements Serializable {

    /**
     * 协议名称
     */
    private String name;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * 协议服务名称
     */
    private String serviceName;
    /**
     * 主机IP
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

    /**
     * 描述
     */
    private String description;

    /**
     * 驱动程序属性
     */
    private List<DriverAttribute> driverAttribute;

    /**
     * 点位属性
     */
    private List<PointAttribute> pointAttribute;

    public DriverDTO(String name, String serviceName, String localHost, Integer port) {
        this.name = name;
        this.serviceName = serviceName;
        this.host = localHost;
        this.port = port;
    }

}
