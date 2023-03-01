package com.pangu.common.sdk.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.ip.AddressUtils;
import com.pangu.common.sdk.bean.DriverProperty;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.sdk.service.DriverMetadataService;
import com.pangu.common.sdk.service.DriverService;
import com.pangu.common.sdk.tuils.ValidateUtil;
import com.pangu.manager.api.RemoteDriverService;
import com.pangu.manager.api.domain.dto.DriverDTO;
import com.pangu.manager.api.enums.OnlineStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    @DubboReference
    private final RemoteDriverService remoteDriverService;

    private final DriverContext driverContext;
    private final DriverProperty driverProperty;
    private final DriverService driverService;
    private final ThreadPoolExecutor threadPoolExecutor;


    @Override
    public void initial() {

        // TODO Auto-generated method stub
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
        } catch (Exception e) {
            close("The driver {}/{} is initialized failed, {}", driver.getServiceName(), driver.getName(), e.getMessage());
        }


//        log.info("The driver {}/{} is initializing", driver.getServiceName(), driver.getName());
//
//        registerHandshake();
//        driverService.driverEventSender(new DriverEvent(
//                serviceName,
//                CommonConstant.Driver.Event.DRIVER_REGISTER,
//                new DriverRegister(
//                        driverProperty.getTenant(),
//                        driver,
//                        driverProperty.getDriverAttribute(),
//                        driverProperty.getPointAttribute()
//                )
//        ));
//        syncDriverMetadata(driver);

       //  log.info("The driver {}/{} is initialized successfully", driver.getServiceName(), driver.getName());

    }


    /**
     * 同步驱动数据
     *
     * @param primaryKey 主键
     */
    private void syncDriverMetadata(String primaryKey) {
        try {
            threadPoolExecutor.submit(() -> {
                driverService.driverMetadataSync(primaryKey);

                while (!OnlineStatus.ONLINE.equals(driverContext.getDriverStatus())) {
                    ThreadUtil.sleep(500);
                }
            }).get(5, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException exception) {
            driverService.close("The driver initialization failed, Sync driver metadata from dc3-center-manager timeout");
        }
    }


    /**
     * 关闭服务
     *
     * @param template 模板
     * @param params   参数个数
     */
    public void close(CharSequence template, Object... params) {

    }

}
