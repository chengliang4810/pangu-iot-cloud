package com.pangu.iot.manager.driver.service.mqtt;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.redis.utils.RedisUtils;
import com.pangu.data.api.RemoteDeviceStatusService;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.service.IDriverService;
import com.pangu.iot.manager.product.domain.Product;
import com.pangu.iot.manager.product.service.IProductService;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.dto.DriverStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Topic(topic = IotConstants.Topic.Driver.DRIVER_TOPIC_HEARTBEAT_SUBSCRIBE_TOPIC, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class DriverHeartbeatReceiver extends MqttConsumer<DriverStatus> {

    private final IDeviceService deviceService;
    private final IDriverService driverService;
    private final IProductService productService;
    @DubboReference
    private RemoteDeviceStatusService deviceStatusService;

    @Override
    protected void messageHandler(String topic, DriverStatus entity) {
        log.debug("收到驱动心跳: {}", entity);
        RedisUtils.setCacheObject(IotConstants.RedisKey.DRIVER_HEARTBEAT + entity.getPrimaryKey(), LocalDateTime.now(), Duration.ofSeconds(30));
        ThreadUtil.execAsync(() -> {
            // 驱动上线
            online(entity);
        });
    }

    /**
     * 使关联该驱动的设备上线
     *
     * @param entity 实体
     */
    private void online(DriverStatus entity) {
        List<String> split = StrUtil.split(entity.getPrimaryKey(), "_");
        if (split.size() != 3) {
            return;
        }
        String driverName = split.get(0);
        Driver driver = driverService.getOne(Wrappers.lambdaQuery(Driver.class).eq(Driver::getName, driverName));
        if (driver == null) {
            return;
        }
        // 使用该驱动的所有产品
        List<Product> productList = productService.list(Wrappers.lambdaQuery(Product.class).eq(Product::getType, 2).eq(Product::getDriver, driver.getId()));
        productList.forEach(product -> {
            // 产品下的所有设备
            deviceService.list(Wrappers.lambdaQuery(Device.class).eq(Device::getProductId, product.getId()))
                // 网关设备上线
                .forEach(device -> deviceStatusService.online(String.valueOf(device.getId()), Math.toIntExact(System.currentTimeMillis() / 1000)));
        });
    }

    @Override
    public DriverStatus decoder(MqttMessage message) {
        byte[] payload = message.getPayload();
        return JsonUtils.parseObject(payload, DriverStatus.class);
    }

}
