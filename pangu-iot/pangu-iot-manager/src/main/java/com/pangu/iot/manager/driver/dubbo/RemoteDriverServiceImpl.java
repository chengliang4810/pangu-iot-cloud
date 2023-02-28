package com.pangu.iot.manager.driver.dubbo;

import com.pangu.iot.manager.driver.service.IDriverSdkService;
import com.pangu.manager.api.RemoteDriverService;
import com.pangu.manager.api.domain.dto.DriverDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;


/**
 * 操作TdEngine服务
 *
 * @author chengliang4810
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteDriverServiceImpl implements RemoteDriverService {

    private final IDriverSdkService driverSdkService;

    @Override
    public void driverRegister(DriverDTO driverDto) {
        driverSdkService.driverRegister(driverDto);
    }


}
