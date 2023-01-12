package com.pangu.common.tdengine.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface TdCommonDao {

    /***
     * 创建数据库
     */
    int  createDB( String database);

    /***
     * 创建超级表
     */
    int  createSTable(String database);


}
