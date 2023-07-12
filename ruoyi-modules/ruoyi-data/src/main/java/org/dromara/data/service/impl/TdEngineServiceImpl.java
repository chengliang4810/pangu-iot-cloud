package org.dromara.data.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.enums.AttributeType;
import org.dromara.data.api.domain.TdColumn;
import org.dromara.data.constant.TableConstants;
import org.dromara.data.domain.SuperTable;
import org.dromara.data.mapper.TdDatabaseMapper;
import org.dromara.data.service.TdEngineService;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.dromara.data.constant.TableConstants.DEVICE_TABLE_NAME_PREFIX;
import static org.dromara.data.constant.TableConstants.SUPER_TABLE_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class TdEngineServiceImpl implements TdEngineService {

    private final DataSource dataSource;
    private final TdDatabaseMapper databaseMapper;

    /**
     * init超级表
     *
     * @param tableName 表名
     */
    @Override
    public void initSuperTable(String tableName) {
        // 创建超级表默认字段
        List<TdColumn> tdColumns = ListUtil.of(new TdColumn(TableConstants.TABLE_PRIMARY_FIELD, "TIMESTAMP"), new TdColumn(TableConstants.TABLE_STATUS_FIELD, "int"));
        List<TdColumn> tags = ListUtil.of(new TdColumn(TableConstants.TABLE_LOCATION_FIELD, "NCHAR(20)"));
        createSuperTable(SUPER_TABLE_PREFIX + tableName, tdColumns, tags);
    }

    /**
     * 创建超级表
     * 超级表中列的最大个数为 4096，需要注意，这里的 4096 是包含 TAG 列在内的，详细内容查看TdEngine官网
     *
     * @param table   表名
     * @param columns 列 至少一个
     * @param tags    标签 至少一个
     * @return {@link String}
     */
    @Override
    public void createSuperTable(String table, List<TdColumn> columns, List<TdColumn> tags) {
        // 参数检查
        Assert.notBlank(table, "table name is blank");
        Assert.notNull(tags, "tags is null");
        Assert.notNull(columns, "columns is null");
        Assert.isTrue(columns.size() > 0, "columns size is zero");
        Assert.isTrue(tags.size() > 0, "tags size is zero");

        SuperTable superTable = new SuperTable();
        superTable.setDatabase(getDatabaseName());
        superTable.setTableName(table);
        superTable.setColumns(columns);
        superTable.setTags(tags);
        databaseMapper.createSuperTable(superTable);
    }

    /**
     * 删除超级表
     *
     * @param table 表
     */
    @Override
    public void dropSuperTable(String table) {
        // 参数检查
        Assert.notNull(table, "id is null");
        // 删除表
        databaseMapper.deleteSuperTable(getDatabaseName(), SUPER_TABLE_PREFIX + table);
    }

    /**
     * 创建超级表字段
     *
     * @param table     table
     * @param key       关键
     * @param valueType 值类型
     */
    @Override
    public void createSuperTableField(String table, String key, String valueType) {
        // 参数检查
        Assert.notNull(table, "id is null");
        Assert.notBlank(key, "key is blank");
        Assert.notBlank(valueType, "valueType is blank");
        // 创建表字段
        databaseMapper.createSuperTableField(getDatabaseName(), SUPER_TABLE_PREFIX + table, key, convertType(valueType));
    }

    /**
     * 删除超级表字段
     *
     * @param table 表格
     * @param key   关键
     */
    @Override
    public void dropSuperTableField(String table, String... key) {
        // 参数检查
        Assert.notNull(table, "id is null");
        Assert.notNull(key, "key is blank");
        // 删除表字段
        for (String k : key) {
            databaseMapper.deleteSuperTableField(getDatabaseName(), SUPER_TABLE_PREFIX + table, k);
        }
    }

    /**
     * 使用超级表创建子表
     *
     * @param superTableName 超级表名
     * @param tableName      表名
     */
    @Override
    public void createTable(String superTableName, String tableName) {
        // 参数检查
        Assert.notBlank(superTableName, "superTableName is blank");
        Assert.notBlank(tableName, "tableName is blank");
        // 创建子表
        databaseMapper.createTable(SUPER_TABLE_PREFIX + superTableName, DEVICE_TABLE_NAME_PREFIX + tableName);
    }

    /**
     * 删除普通表/子表
     *
     * @param tableNameList 表名称列表
     */
    @Override
    public void dropTable(List<String> tableNameList) {
        Assert.notEmpty(tableNameList, "表名称列表不能为空");
        List<String> resultList = tableNameList.stream().map(tableName -> DEVICE_TABLE_NAME_PREFIX + tableName).collect(Collectors.toList());
        databaseMapper.dropTable(resultList);
    }

    /**
     * 选择最后一个数据
     * 查询当日最后一条数据
     *
     * @param table 表格
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> selectLastRowData(String table) {
        Map<String, Object> lastRowData = databaseMapper.selectLastRowData(table);
        if (MapUtil.isEmpty(lastRowData)) {
            return Collections.emptyMap();
        }
        // 处理Key 删除last_row()
        Map<String, Object> result = MapUtil.newHashMap(lastRowData.size());
        lastRowData.forEach((key, value) -> {
            if (key.contains("last_row(")) {
                key = key.substring(9, key.length() - 1);
            }
            result.put(key, value);
        });
        return result;
    }

    /**
     * 查询每列最后数据
     *
     * @param table 表格
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> selectLastData(String table) {
        Map<String, Object> lastRowData = databaseMapper.selectLastData(table);
        if (MapUtil.isEmpty(lastRowData)) {
            return Collections.emptyMap();
        }
        // 处理Key 删除last()
        Map<String, Object> result = MapUtil.newHashMap(lastRowData.size());
        lastRowData.forEach((key, value) -> {
            if (key.contains("last_row(")) {
                key = key.substring(5, key.length() - 1);
            }
            result.put(key, value);
        });
        return result;
    }

    /**
     * 插入数据
     *
     * @param table      表格
     * @param superTable 超级表
     * @param value      值 key-values结构
     * @return int 插入条数
     */
    @Override
    public int insertData(String table, String superTable, Map<String, Object> value) {
        int data = 0;
        try {
            data = databaseMapper.insertData(table, superTable, value, new Object[]{1});
        } catch (Exception e) {
            log.error("插入数据失败", e);
        }
        return data;
    }

    /**
     * 属性类型转换为TdEngine类型
     * TODO 特别注意String类型长度，这里默认为100，如有需要请自行修改，后续考虑使用配置文件
     *
     * @param valueType 值类型
     * @return {@link String}
     */
    private String convertType(String valueType) {
        AttributeType attributeType = AttributeType.ofCode(valueType);
        if (attributeType == null) {
            // 匹配失败，尝试使用名称匹配
            attributeType = AttributeType.ofName(valueType);
        }
        Assert.notNull(attributeType, "valueType is not match");
        return switch (attributeType) {
            case INT -> "INT";
            case LONG -> "BIGINT";
            case FLOAT -> "FLOAT";
            case DOUBLE -> "DOUBLE";
            case BOOLEAN -> "BOOL";
            case STRING -> "NCHAR(100)";
        };
    }

    @SneakyThrows
    private String getJdbcUrl() {
        return dataSource.getConnection().getMetaData().getURL();
    }

    private String getDatabaseName() {
        String jdbcUrl = getJdbcUrl();
        // 截取数据库名称
        int startIndex = jdbcUrl.lastIndexOf("/") + 1;
        int endIndex = jdbcUrl.indexOf("?", startIndex);
        if (endIndex == -1) {
            endIndex = jdbcUrl.length();
        }
        return jdbcUrl.substring(startIndex, endIndex);
    }

}
