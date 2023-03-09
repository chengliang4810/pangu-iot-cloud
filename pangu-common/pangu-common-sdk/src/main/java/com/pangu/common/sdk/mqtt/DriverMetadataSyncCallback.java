package com.pangu.common.sdk.mqtt;

import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.manager.api.RemoteDriverService;
import com.pangu.manager.api.domain.DriverMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 驱动程序元数据同步回调
 * 通知驱动程序重新获取最新的配置信息
 * @author chengliang
 * @date 2023/03/08
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Topic(topic = "iot/driver/${spring.application.name}/metadata/sync", qos = 2)
public class DriverMetadataSyncCallback extends MqttConsumer<String> {

    @Value("${spring.application.name}")
    private String serviceName;

    private final DriverContext driverContext;
    @DubboReference
    private final RemoteDriverService remoteDriverService;

    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param entity 实体
     */
    @Override
    protected void messageHandler(String topic, String entity) {
        DriverMetadata driverMetadata = remoteDriverService.driverMetadataSync(serviceName);
        driverContext.setDriverMetadata(driverMetadata);
    }

    /**
     * 解码器
     *
     * @param message 消息
     * @return {@link }
     */
    @Override
    public String decoder(MqttMessage message) {
        byte[] payload = message.getPayload();
        return new String(payload);
    }

}
