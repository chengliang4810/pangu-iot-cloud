package com.pangu.common.tdengine.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.pangu.common.tdengine.model.SuperTableDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * td数据库 库、表操作方法
 *
 * @author chengliang
 * @date 2023/01/12
 */
@Mapper
@InterceptorIgnore(tenantLine = "true", dataPermission = "true")
public interface TdDatabaseMapper {

    /***
     * 创建数据库
     */
    void  createDB(String database);

    /***
     * 创建超级表
     */
    void  createSuperTable(SuperTableDTO table);

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
     * @param table     表格
     * @param key       关键
     * @param valueType 值类型
     */
    @Insert("ALTER STABLE ${database}.${table} ADD COLUMN ${key} ${valueType}")
    void createSuperTableField(@Param("database") String database, @Param("table") String table, @Param("key") String key, @Param("valueType") String valueType);

    /**
     * 删除超级表字段
     *
     * @param database 数据库
     * @param table    表格
     * @param key      关键
     */
    @Delete("ALTER STABLE ${database}.${table} DROP COLUMN ${key}")
    void deleteSuperTableField(@Param("database") String database, @Param("table") String table, @Param("key") String key);

    /**
     * 删除超级表
     *
     * @param database 数据库
     * @param table    表格
     */
    @Delete("DROP STABLE IF EXISTS ${database}.${table}")
    void deleteSuperTable(@Param("database") String database, @Param("table") String table);

}
