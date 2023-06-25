package org.dromara.common.emqx.utils;

import cn.hutool.core.util.StrUtil;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.emqx.doamin.EmqxClient;

public class EmqxUtil {

    public static EmqxClient getClient() {
        return SpringUtils.getBean(EmqxClient.class);
    }

    public static <T> T getClient(Class<T> tClass) {
        return SpringUtils.getBean(tClass);
    }

    public static void publish(String topic, String payload) {
        publish(topic, payload, 0);
    }

    public static void publish(String topic, String payload, int qos) {
        if (StrUtil.isBlank(payload)){
            payload = "";
        }
        publish(topic, payload.getBytes(), qos);
    }

    public static void publish(String topic, byte[] payload) {
        publish(topic, payload, 0);
    }

    public static void publish(String topic, byte[] payload, int qos) {
        publish(topic, payload, qos, false);
    }

    public static void publish(String topic, byte[] payload, int qos, boolean retained) {
        getClient().publish(topic, payload, qos, retained);
    }

}
