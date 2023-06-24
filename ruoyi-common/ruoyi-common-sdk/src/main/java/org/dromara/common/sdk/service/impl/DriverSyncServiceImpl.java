package org.dromara.common.sdk.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.common.core.constant.SymbolConstants;
import org.dromara.common.emqx.utils.EmqxUtil;
import org.dromara.common.iot.constant.DriverTopic;
import org.dromara.common.iot.dto.DriverDTO;
import org.dromara.common.iot.dto.DriverSyncDownDTO;
import org.dromara.common.iot.dto.DriverSyncUpDTO;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.iot.enums.DriverStatusEnum;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.sdk.DriverContext;
import org.dromara.common.sdk.property.DriverProperty;
import org.dromara.common.sdk.service.DriverSenderService;
import org.dromara.common.sdk.service.DriverSyncService;
import org.dromara.common.sdk.utils.AddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 驱动同步相关接口实现
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
@Service
public class DriverSyncServiceImpl implements DriverSyncService {

    @Autowired
    private DriverContext driverContext;
    @Autowired
    private DriverProperty driverProperty;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.port}")
    private Integer servicePort;

    @Resource
    private DriverSenderService driverSenderService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void up() {
        try {
            DriverSyncUpDTO entityDTO = buildRegisterDTOByProperty();
            // 驱动唯一标识： 应用名称_IP地址_IP端口
            String driverUniqueKey = applicationName + SymbolConstants.UNDERSCORE + entityDTO.getDriver().getServiceHost() + SymbolConstants.UNDERSCORE + servicePort;
            log.info("The driver {} is initializing", applicationName);
            log.debug("The driver {} initialization information is: {}", driverProperty.getName(), JSONUtil.toJsonPrettyStr(entityDTO));
            // 发送驱动注册信息
            EmqxUtil.getClient().publish(DriverTopic.getDriverRegisterTopic(driverUniqueKey), JsonUtils.toJsonString(entityDTO), 2);

            threadPoolExecutor.submit(() -> {
                while (!DriverStatusEnum.ONLINE.equals(driverContext.getDriverStatus())) {
                    ThreadUtil.sleep(500);
                }
            }).get(15, TimeUnit.SECONDS);

            log.info("The driver {} is initialized successfully.", entityDTO.getDriver().getDriverName());
        } catch (Exception ignored) {
            ignored.printStackTrace();
            log.error("The driver initialization failed, registration response timed out.");
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    @Override
    public void down(DriverSyncDownDTO entityDTO) {

        if (StringUtils.isBlank(entityDTO.getContent())){
            return;
        }

        DriverMetadata driverMetadata = JsonUtils.parseObject(entityDTO.getContent(), DriverMetadata.class);
        if (ObjectUtils.anyNull(driverMetadata)) {
            driverMetadata = new DriverMetadata();
        }
        driverContext.setDriverMetadata(driverMetadata);
        driverContext.setDriverStatus(DriverStatusEnum.ONLINE);
        driverMetadata.getDriverAttributeMap().values().forEach(driverAttribute -> log.info("Syncing driver attribute[{}] metadata: {}", driverAttribute.getAttributeName(), JSONUtil.toJsonPrettyStr(driverAttribute)));
        driverMetadata.getPointAttributeMap().values().forEach(pointAttribute -> log.info("Syncing point attribute[{}] metadata: {}", pointAttribute.getAttributeName(), JSONUtil.toJsonPrettyStr(pointAttribute)));
        driverMetadata.getDeviceMap().values().forEach(device -> log.info("Syncing device[{}] metadata: {}", device.getDeviceName(), JSONUtil.toJsonPrettyStr(device)));
        driverMetadata.getGatewayDeviceMap().values().forEach(device -> log.info("Syncing gateway device[{}] metadata: {}", device.getDeviceName(), JSONUtil.toJsonPrettyStr(device)));
        log.info("The metadata synced successfully.");
    }

    /**
     * Property To DriverRegisterDTO
     *
     * @return DriverRegisterDTO
     */
    private DriverSyncUpDTO buildRegisterDTOByProperty() {
        DriverSyncUpDTO driverSyncUpDTO = new DriverSyncUpDTO();
        driverSyncUpDTO.setDriver(buildDriverByProperty());
        driverSyncUpDTO.setTenant(driverProperty.getTenant());
        driverSyncUpDTO.setDriverAttributes(driverProperty.getDriverAttribute());
        driverSyncUpDTO.setPointAttributes(driverProperty.getPointAttribute());
        return driverSyncUpDTO;
    }

    /**
     * Property To Driver
     *
     * @return Driver
     */
    private DriverDTO buildDriverByProperty() {
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDriverName(driverProperty.getName());
        driverDTO.setDriverCode(driverProperty.getCode());
        driverDTO.setServiceName(applicationName);
        driverDTO.setServiceHost(AddressUtils.localHost());
        driverDTO.setServicePort(servicePort);
        driverDTO.setRemark(driverProperty.getRemark());
        return driverDTO;
    }

}
