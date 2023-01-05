package com.pangu.common.core.constant;

/**
 * 常量
 *
 * @author chengliang
 */
public interface CommonConstant {

    interface Topic {

        interface PointValue {
            /**
             * 设备读点位值
             * device/1552478436867022849/read/1534427673043521537/value
             */
            String DEVICE_READ_POINT_VALUE_TPL = "device/{deviceId}/read/data";
            /**
             * 订阅设备读点位值
             */
            String DEVICE_READ_POINT_VALUE_SUBSCRIPTION = "device/+/read/data";
            /**
             * 设备写点位值
             */
            String DEVICE_WRITE_POINT_VALUE_TPL = "device/{deviceId}/write/{pointId}/data";
            /**
             * 订阅设备写点位值
             */
            String DEVICE_WRITE_POINT_VALUE_SUBSCRIPTION = "device/+/write/+/data";
        }

    }

    /**
     * 符号相关
     */
    interface Symbol {
        /**
         * 点
         */
        String DOT = ".";

        /**
         * 下划线
         */
        String UNDERSCORE = "_";

        /**
         * 星号
         */
        String ASTERISK = "*";

        /**
         * 井号
         */
        String HASHTAG = "#";

        /**
         * 分隔符
         */
        String SEPARATOR = "::";

        /**
         * 斜线
         */
        String SLASH = "/";
    }

    /**
     * 消息相关
     */
    interface Rabbit {
        // Arguments
        String MESSAGE_TTL = "x-message-ttl";

        String TOPIC_EXCHANGE_ZABBIX_INPUT = "pangu.exchange.zabbix.input.data";
        String ROUTING_ZABBIX_INPUT_VALUE = "zabbix_route_input";
        String QUEUE_ZABBIX_INPUT_VALUE = "queue.zabbix.input.data";

        // zabbix output 发送zabbix数据
        String TOPIC_EXCHANGE_ZABBIX_OUTPUT = "pangu.exchange.zabbix.output.data";
        String ROUTING_ZABBIX_OUTPUT_VALUE = "zabbix_route_output";
        String QUEUE_ZABBIX_OUTPUT_VALUE = "queue.zabbix.output.data";
    }

}
