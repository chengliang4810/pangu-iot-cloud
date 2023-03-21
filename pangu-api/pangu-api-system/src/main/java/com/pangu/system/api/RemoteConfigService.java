package com.pangu.system.api;

/**
 * Config服务
 *
 * @author chengliang4810
 */
public interface RemoteConfigService {

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey key
     * @return 值
     */
    String getConfigByKey(String configKey);

    /**
     * 修改参数配置值
     *
     * @param key   key
     * @param value value
     * @return {@link String}
     */
    Boolean editValue(String key, String value);

}
