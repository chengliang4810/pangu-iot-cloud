package com.pangu.data.api;

import com.pangu.data.api.model.TdColumnDTO;

import java.util.List;

/**
 * TdEngine服务
 *
 * @author chengliang4810
 */
public interface RemoteTdEngineService {

    /**
     * 创建超级表
     */
    void createSuperTable(String table, List<TdColumnDTO> columns, List<TdColumnDTO> tags);

    /**
     * 初始化超级表
     */
    void initSuperTable(String table);

    /**
     * 删除超级表
     *
     * @param table 表格
     */
    void deleteSuperTable(String table);

    /**
     * 创建表字段
     *
     * @param table      table
     * @param key       关键
     * @param valueType 值类型
     */
    void createSuperTableField(String table, String key, String valueType);

    /**
     * 删除超级表字段
     *
     * @param table 表格
     * @param key   关键
     */
    void deleteSuperTableField(String table, String key);
}
