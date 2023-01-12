package com.pangu.common.tdengine.mapper;

import com.pangu.common.tdengine.model.SuperTableDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * td数据库 库、表操作方法
 *
 * @author chengliang
 * @date 2023/01/12
 */
@Mapper
public interface TdDatabaseMapper {

    /***
     * 创建数据库
     */
    int  createDB(String database);

    /***
     * 创建超级表
     */
    int  createSuperTable(SuperTableDTO table);


}
