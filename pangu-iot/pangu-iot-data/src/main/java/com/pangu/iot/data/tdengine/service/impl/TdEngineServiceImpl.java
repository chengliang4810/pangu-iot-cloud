package com.pangu.iot.data.tdengine.service.impl;

import cn.hutool.core.map.MapUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import com.pangu.common.tdengine.model.SuperTableDTO;
import com.pangu.common.tdengine.model.TdColumn;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.core.constant.IotConstants.TD_DB_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class TdEngineServiceImpl implements TdEngineService {


    private final TdDatabaseMapper databaseMapper;


    /**
     * 删除表
     *
     * @param tableNameList 表名称列表
     */
    @Override
    public void dropTable(List<String> tableNameList) {
        Assert.notEmpty(tableNameList, "表名称列表不能为空");
        List<String> resultList = tableNameList.stream().map(tableName -> IotConstants.DEVICE_TABLE_NAME_PREFIX + tableName).collect(Collectors.toList());
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
    public Map<String, Object> selectLastData(String table) {
        Map<String, Object> lastRowData = databaseMapper.selectTodayLastRowData(table);
        if (MapUtil.isEmpty(lastRowData)){
            return Collections.emptyMap();
        }
        // 处理Key 删除last_row()
        Map<String, Object> result = MapUtil.newHashMap(lastRowData.size());
        lastRowData.forEach((key, value) -> {
            String newKey = key.substring(9, key.length() - 1);
            result.put(newKey, value);
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
        Assert.isGreaterZero(columns.size(), "columns size is zero");
        Assert.isGreaterZero(tags.size(), "tags size is zero");

        //
        SuperTableDTO superTableDTO = new SuperTableDTO();
        superTableDTO.setDatabase(TD_DB_NAME);
        superTableDTO.setTableName(table);
        superTableDTO.setColumns(columns);
        superTableDTO.setTags(tags);
        databaseMapper.createSuperTable(superTableDTO);
    }

    /**
     * 创建表字段
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
        String type = "NCHAR(50)";
        switch (valueType){
            case "0":
                type = "float";
                break;
            case "3":
                type = "int";
                break;
        }
        // 创建表字段
        databaseMapper.createSuperTableField(TD_DB_NAME, table, key, type);
    }


    /**
     * 删除超级表字段
     *
     * @param table 表格
     * @param key   关键
     */
    @Override
    public void deleteSuperTableField(String table, String... key) {
        // 参数检查
        Assert.notNull(table, "id is null");
        Assert.notNull(key, "key is blank");
        // 删除表字段
        for (String k : key) {
            databaseMapper.deleteSuperTableField(TD_DB_NAME, table, k);
        }
    }


    @Override
    public void deleteSuperTable(String table) {
        // 参数检查
        Assert.notNull(table, "id is null");
        // 删除表
        databaseMapper.deleteSuperTable(TD_DB_NAME, table);
    }
}
