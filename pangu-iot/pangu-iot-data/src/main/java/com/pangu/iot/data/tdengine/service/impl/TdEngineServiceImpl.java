package com.pangu.iot.data.tdengine.service.impl;

import com.pangu.common.core.utils.Assert;
import com.pangu.common.tdengine.mapper.TdDatabaseMapper;
import com.pangu.common.tdengine.model.SuperTableDTO;
import com.pangu.common.tdengine.model.TdColumn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pangu.common.core.constant.IotConstants.TD_DB_NAME;

@Slf4j
@Service
@RequiredArgsConstructor
public class TdEngineServiceImpl implements com.pangu.iot.data.tdengine.service.TdEngineService {


    private final TdDatabaseMapper databaseMapper;

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
