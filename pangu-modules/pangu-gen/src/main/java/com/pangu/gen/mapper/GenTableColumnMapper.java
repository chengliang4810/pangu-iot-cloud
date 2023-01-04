package com.pangu.gen.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.gen.domain.GenTableColumn;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author chengliang4810
 */
@InterceptorIgnore(dataPermission = "true")
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumnMapper, GenTableColumn, GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
