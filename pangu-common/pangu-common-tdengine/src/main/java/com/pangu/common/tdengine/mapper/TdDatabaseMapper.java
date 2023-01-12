package com.pangu.common.tdengine.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TdDatabaseMapper {

    /***
     * 创建数据库
     */
    int  createDB( String database);

    /***
     * 创建超级表
     */
    int  createSTable(String database);


}
