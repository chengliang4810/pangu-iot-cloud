package org.dromara.data.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.data.domain.SuperTable;

import java.util.List;
import java.util.Map;

/**
 * td数据库 库、表操作方法
 *
 * @author chengliang
 * @date 2023/01/12
 */
@InterceptorIgnore(tenantLine = "true", dataPermission = "true")
public interface TdDatabaseMapper {

    /***
     * 创建超级表
     */
    void  createSuperTable(SuperTable table);

    /**
     * 展示数据库创建sql
     *
     * @param database 数据库
     * @return {@link String}
     */
    String showDbCreateSql(String database);

    /**
     * 创建super表字段
     *
     * @param database  数据库
     * @param tableName 表名
     * @param key       关键
     * @param valueType 值类型
     */
    @Insert("ALTER STABLE ${database}.${table} ADD COLUMN #{key} ${valueType}")
    void createSuperTableField(@Param("database") String database, @Param("table") String tableName, @Param("key") String key, @Param("valueType") String valueType);

    /**
     * 删除超级表字段
     *
     * @param database 数据库
     * @param tableName 表名
     * @param key      关键
     */
    @Delete("ALTER STABLE ${database}.${table} DROP COLUMN #{key}")
    void deleteSuperTableField(@Param("database") String database, @Param("table") String tableName, @Param("key") String key);

    /**
     * 删除超级表
     *
     * @param database 数据库
     * @param tableName 表名
     */
    @Delete("DROP STABLE IF EXISTS ${database}.${table}")
    void deleteSuperTable(@Param("database") String database, @Param("table") String tableName);

    /**
     * 插入数据
     *
     * @param tableName 表名
     * @param value      值 key - value1,value2
     * @param tags       标签
     * @return int
     */
    int insertData(@Param("table") String tableName, @Param("value") Map<String,Object> value, @Param("tags") Object[] tags);

    /**
     * 查询当日最后一行数据
     *
     * @param tableName 表名
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Select("select LAST_ROW(*) from ${table} where ts > TODAY()")
    Map<String, Object> selectTodayLastRowData(@Param("table") String tableName);

    /**
     * 查询最后一行数据
     *
     * @param tableName 表名
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Select("select LAST_ROW(*) from ${table}")
    Map<String, Object> selectLastRowData(@Param("table") String tableName);

    /**
     * 查询最后一行数据
     *
     * @param tableName 表名
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Select("select LAST(*) from ${table}")
    Map<String, Object> selectLastData(@Param("table") String tableName);


    /**
     * 使用超级表创建子表
     * @param superTableName 超级表名
     * @param tableName      表名
     */
    @Insert("CREATE TABLE  IF NOT EXISTS ${table} USING ${superTable}(location) TAGS('')")
    void createTable(@Param("superTable") String superTableName, @Param("table") String tableName);

    /**
     * 删除表
     *
     * @param tables 结果列表
     */
    void dropTable(@Param("tables") List<String> tables);
}
