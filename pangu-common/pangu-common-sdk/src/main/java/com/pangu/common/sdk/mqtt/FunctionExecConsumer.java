package com.pangu.common.sdk.mqtt;

import com.pangu.common.core.domain.dto.DeviceExecuteResult;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.emqx.annotation.Topic;
import com.pangu.common.emqx.constant.Pattern;
import com.pangu.common.emqx.core.MqttConsumer;
import com.pangu.common.sdk.context.DriverContext;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.sdk.service.DriverService;
import com.pangu.common.zabbix.model.DeviceFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Topic(topic = "iot/device/+/function/+/exec", qos = 2, patten = Pattern.SHARE, group = "${spring.application.name}Group")
public class FunctionExecConsumer extends MqttConsumer<DeviceFunction> {

    @Autowired(required = false)
    private DriverDataService driverDataService;
    private final DriverContext driverContext;
    private final DriverService driverService;


    /**
     * 消息处理程序,业务操作
     *
     * @param topic  主题
     * @param deviceFunction 实体
     */
    @Override
    protected void messageHandler(String topic, DeviceFunction deviceFunction) {

        if (driverDataService == null) {
            log.error("未实现DriverDataService接口");
            return;
        }

        log.debug("topic: {}, deviceFunction: {}", topic, JsonUtils.toJsonString(deviceFunction));
        // 设备是否属于该驱动， 属于则调用执行功能方法
        if (driverContext.getDriverMetadata().getDeviceMap().containsKey(deviceFunction.getDeviceId())) {

            // 捕捉异常，记录日志
            DeviceExecuteResult deviceExecuteResult = new DeviceExecuteResult().setDeviceId(deviceFunction.getDeviceId()).setSuccess(false).setIdentifier(deviceFunction.getIdentifier()).setUuid(deviceFunction.getUuid());
            try {
                Boolean result = driverDataService.control(deviceFunction);
                deviceExecuteResult.setSuccess(result);
                log.info("控制设备: {} , 参数: {}", result, deviceFunction);
            } catch (Exception e){
                log.error("控制设备失败: ", e);
            } finally {
                // 通知管理平台执行结果
                driverService.notifyDeviceFunctionResult(deviceExecuteResult);
            }
            return;
        }
        log.debug("设备不属于该驱动管理: {}", deviceFunction.getDeviceId());
    }

    /**
     * 解码器
     *
     * @param message 消息
     */
    @Override
    public DeviceFunction decoder(MqttMessage message) {
        return JsonUtils.parseObject(message.getPayload(), DeviceFunction.class);
    }


}
