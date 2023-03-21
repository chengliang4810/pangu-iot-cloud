package com.pangu.data.api.model;

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
public class TdColumnDTO {

    /**
     * 名字
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    public TdColumnDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }

}
