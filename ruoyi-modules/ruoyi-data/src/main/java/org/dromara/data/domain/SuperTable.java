package org.dromara.data.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.data.api.domain.TdColumn;

import java.io.Serializable;
import java.util.List;

/**
 * 超级表dto
 *
 * @author chengliang
 * @date 2023/01/12
 */
@Data
@Accessors(chain = true)
public class SuperTable implements Serializable {

    /**
     * 数据库
     */
    private String database;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 列
     */
    private List<TdColumn> columns;

    /**
     * 标签
     */
    private List<TdColumn> tags;

}
