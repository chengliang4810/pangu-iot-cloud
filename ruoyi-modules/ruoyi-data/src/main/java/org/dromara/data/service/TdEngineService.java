package org.dromara.data.service;

import org.dromara.data.TdColumn;

import java.util.List;
import java.util.Map;

public interface TdEngineService {

    /**
     * 创建超级表
     * 超级表中列的最大个数为 4096，需要注意，这里的 4096 是包含 TAG 列在内的，详细内容查看TdEngine官网
     * @param table   表名
     * @param columns 列 至少一个
     * @param tags  标签 至少一个
     * @return {@link String}
     */
    void createSuperTable(String table, List<TdColumn> columns, List<TdColumn> tags);

    /**
     * 创建表字段
     *
     * @param table     表名
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
    void deleteSuperTableField(String table, String... key);

    /**
     * 删除超级表
     *
     * @param table 表格
     */
    void deleteSuperTable(String table);


    /**
     * 插入数据
     *
     * @param table      表格
     * @param superTable 超级表
     * @param value      值 key-values结构
     * @return int 插入条数
     */
    int insertData(String table, String superTable, Map<String, Object> value);


    /**
     * 选择最后一个数据
     * 查询当日最后一条数据
     *
     * @param table 表格
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> selectLastData(String table);

    /**
     * 选择最后一行数据
     *
     * @param table 表格
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    Map<String, Object> selectLastRowData(String table);

    /**
     * 删除表
     *
     * @param tableNameList 表名称列表
     */
    void dropTable(List<String> tableNameList);

}
