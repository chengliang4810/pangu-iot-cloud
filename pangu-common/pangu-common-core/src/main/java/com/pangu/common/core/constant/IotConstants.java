package com.pangu.common.core.constant;

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
     * 设备表名模板
     */
    String DEVICE_TABLE_NAME_TEMPLATE = "d_{}_{}";

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
}
