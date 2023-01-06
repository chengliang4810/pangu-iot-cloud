package com.pangu.common.zabbix.service;

/**
 * 主机组回调
 *
 * @author chengliang4810
 * @date 2023/01/06 11:07
 */
public interface HostGroupCallback {

    /**
     * 全局主机分组初始化后返回的分组ID，自定义实现存储、数据库、redis等
     *
     * @param groupId 组id
     */
    void initAfter(String groupId);

}
