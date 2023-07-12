package org.dromara.common.iot.entity.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.common.core.enums.AttributeType;

/**
 * 属性配置
 *
 * @author pnoker, chengliang4810
 * @date 2023/06/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeInfo {
    /**
     * 值，string，需要通过type确定真实的数据类型
     */
    private String value;

    /**
     * 类型，value type，用于确定value的真实类型
     */
    private AttributeType type;
}
