package org.dromara.common.iot.constant;

/**
 * 驱动相关主题
 *
 * @author chengliang
 * @date 2023/06/24
 */
public interface DriverTopic {

    /**
     * 驱动程序注册主题模板
     */
    String DRIVER_REGISTER_TOPIC_TPL = "driver/{}/sync/register";

    /**
     * 获取驱动程序注册主题
     *
     * @param driverUniqueKey 驱动唯一标识
     * @return {@link String}
     */
    static String getDriverRegisterTopic(String driverUniqueKey) {
        return DRIVER_REGISTER_TOPIC_TPL.replace("{}", driverUniqueKey);
    }

    /**
     * 驱动程序注册订阅主题
     */
    String DRIVER_REGISTER_SUB_TOPIC = "driver/+/sync/register";

    /**
     * 驱动程序注册回调主题
     */
    String DRIVER_REGISTER_BACK_TOPIC_TPL = "driver/{}/sync/register/back";

    /**
     * 获取驱动程序注册回调主题
     *
     * @param driverUniqueKey 驱动唯一标识
     * @return {@link String}
     */
    static String getDriverRegisterBackTopic(String driverUniqueKey) {
        return DRIVER_REGISTER_BACK_TOPIC_TPL.replace("{}", driverUniqueKey);
    }

    /**
     * 驱动程序注册回调订阅主题
     */
    String DRIVER_REGISTER_BACK_SUB_TOPIC = "driver/+/sync/register/back";

}
