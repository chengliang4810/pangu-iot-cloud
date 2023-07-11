package org.dromara.common.iot.entity.point;


import lombok.*;

import java.io.Serializable;


/**
 * 模板配置信息表
 *
 * @author pnoker, chengliang4810
 * @date 2023/07/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointAttribute implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性类型标识
     */
    private String attributeTypeFlag;

    /**
     * 必填
     */
    private Boolean required = true;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 描述
     */
    private String remark;
}
