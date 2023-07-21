package org.dromara.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 驱动属性值对象 iot_point_attribute_value
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_point_attribute_value")
public class PointAttributeValue extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 点位属性ID
     */
    private Long pointAttributeId;

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 物模型属性ID
     */
    private Long deviceAttributeId;

    /**
     * 属性值
     */
    private String value;


}
