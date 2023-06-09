package com.pangu.common.sdk.service.impl;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.ip.AddressUtils;
import com.pangu.common.sdk.bean.DriverProperty;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.sdk.service.DriverMetadataService;
import com.pangu.common.sdk.service.DriverService;
import com.pangu.common.sdk.utils.ValidateUtil;
import com.pangu.manager.api.RemoteDriverService;
import com.pangu.manager.api.domain.DriverMetadata;
import com.pangu.manager.api.domain.dto.DriverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 驱动程序元数据服务实现
 *
 * @author chengliang
 * @date 2023/02/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DriverMetadataServiceImpl implements DriverMetadataService {

    @Value("${server.port}")
    private int port;
    @Value("${spring.application.name}")
    private String serviceName;
    @DubboReference(timeout = 30000)
    private final RemoteDriverService remoteDriverService;

    private final DriverContext driverContext;
    private final DriverProperty driverProperty;
    private final DriverService driverService;

    @Override
    public void initial() {

        String localHost = AddressUtils.localHost();
        if (!ValidateUtil.isName(driverProperty.getName()) || !ValidateUtil.isName(this.serviceName) || !ValidateUtil.isHost(localHost)) {
            throw new ServiceException("The driver name, service name or host name format is invalid");
        }

        // 驱动信息
        DriverDTO driver = new DriverDTO(driverProperty.getName(), this.serviceName, localHost, this.port);
        driver.setDescription(driverProperty.getDescription());
        driver.setDisplayName(driverProperty.getDisplayName());
        // 驱动属性 & 点位属性
        driver.setDriverAttribute(driverProperty.getDriverAttribute());
        driver.setPointAttribute(driverProperty.getPointAttribute());

        try {
            remoteDriverService.driverRegister(driver);

            DriverMetadata driverMetadata = remoteDriverService.driverMetadataSync(driver.getName());
            driverContext.setDriverMetadata(driverMetadata);
        } catch (Exception e) {
            driverService.close("The driver {}/{} is initialized failed, {}", driver.getServiceName(), driver.getName(), e.getMessage());
        }

    }

}
