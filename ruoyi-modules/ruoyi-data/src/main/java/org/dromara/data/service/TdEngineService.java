package org.dromara.data.service;

import org.dromara.data.api.domain.TdColumn;

import java.util.List;
import java.util.Map;

public interface TdEngineService {

    /**
     * init超级表
     *
     * @param tableName 表名
     */
    void initSuperTable(String tableName);

    /**
     * 删除超级表
     *
     * @param table 表格
     */
    void dropSuperTable(String table);

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
     * @param superTableName     表名
     * @param fieldName       关键
     * @param valueType 值类型
     */
    void createSuperTableField(String superTableName, String fieldName, String valueType);

    /**
     * 删除超级表字段
     *
     * @param table 表格
     * @param fieldName   关键
     */
    void dropSuperTableField(String table, String... fieldName);

    /**
     * 使用超级表创建子表
     *
     * @param superTableName 超级表名
     * @param tableName      表名
     */
    void createTable(String superTableName, String tableName);

    /**
     * 插入数据
     *
     * @param tableName  表名
     * @param value      值 fieldName-values结构
     * @return int 插入条数
     */
    int insertData(String tableName, Map<String, Object> value);


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
