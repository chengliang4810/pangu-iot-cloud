package org.dromara.data.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TdEngine列 信息
 *
 * @author chengliang
 * @date 2023/01/12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TdColumn {

    /**
     * 名字
     */
    private String name;

    /**
     * 数据类型
     */
    private String type;

    public TdColumn(String name, String type) {
        this.name = name;
        this.type = type;
    }

}
