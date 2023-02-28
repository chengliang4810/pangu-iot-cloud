package com.pangu.common.core.constant;

import cn.hutool.core.util.StrUtil;

/**
 * 物联网常量
 *
 * @author chengliang4810
 * @date 2023/01/31 10:35
 */
public interface IotConstants {

    /**
     * tdengine默认数据库名称
     */
    String TD_DB_NAME = "pangu_iot";

    /**
     * 表主要字段
     */
    String TABLE_PRIMARY_FIELD = "ts";

    /**
     * 表状态字段
     */
    String TABLE_STATUS_FIELD = "online";

    /**
     * 超级表前缀
     */
    String SUPER_TABLE_PREFIX = "product_";

    /**
     * 默认标签占位
     */
    String TAG_OCCUPY_KEY = "`pangu_tag`";

    /**
     * 产品id标记名称
     */
    String PRODUCT_ID_TAG_NAME = "__product_id__";

    /**
     * 设备表名前缀
     */
    String DEVICE_TABLE_NAME_PREFIX = "device_";

    /**
     * 报警标记名称
     */
    String ALARM_TAG_NAME   = "__alarm__";
    /**
     * 执行标记名称
     */
    String EXECUTE_TAG_NAME = "__execute__";

    /**
     * 设备状态离线标签
     */
    String DEVICE_STATUS_OFFLINE_TAG = "__offline__";


    /**
     * 设备状态在线标记
     */
    String DEVICE_STATUS_ONLINE_TAG = "__online__";

    /**
     * 设备状态缓存前缀
     */
    String DEVICE_STATUS_CACHE_PREFIX = "com.pangu.iot.data.device_status_";

    /**
     * 属性关键标记名称
     */
    String ATTRIBUTE_KEY_TAG_NAME = "__attribute_key__";


    /**
     * 设备ID缓存前缀
     */
    String DEVICE_CODE_CACHE_PREFIX = "com:pangu:iot:device:code:";


    /**
     * Topic主题相关
     *
     * @author chengliang
     * @date 2023/02/21
     */
    interface Topic {

        interface Driver {

            String DRIVER_TOPIC_HEARTBEAT_URI_TPL = "iot/driver/heartbeat/{}?brokerUrl={}&clientId={}&qos=0&password={}&username={}";

            String DRIVER_TOPIC_HEARTBEAT_SUBSCRIBE_TOPIC = "iot/driver/heartbeat/+";

            public default String getDriverTopicHeartbeatUri(String primaryKey, String brokerUrl, String clientId ,String username, String password) {
                return StrUtil.format(DRIVER_TOPIC_HEARTBEAT_URI_TPL, primaryKey, brokerUrl, clientId, password, username);
            }
        }


        /**
         * 设备功能执行主题
         */
        String DEVICE_EXECUTE_TOPIC_TPL = "iot/device/{}/function/{}/exec";

        /**
         * 设备功能执行结果主题
         */
        String DEVICE_EXECUTE_RESULT_TOPIC_TPL = "iot/device/{}/function/{}/result";

        /**
         * 设备功能执行订阅主题
         */
        String DEVICE_EXECUTE_SUBSCRIBE_TOPIC = "iot/device/+/function/+/exec";

        /**
         * 设备功能执行订阅主题
         */
        String DEVICE_EXECUTE_SHARE_SUBSCRIBE_TOPIC = "$share/{}Group/iot/device/#/function/#/exec";

    }


    interface RedisKey {


        String DRIVER_HEARTBEAT = "com.pangu.iot.driver.heartbeat.";
    }
}
