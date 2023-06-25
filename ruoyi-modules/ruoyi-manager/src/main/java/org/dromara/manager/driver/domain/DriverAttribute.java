package org.dromara.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 驱动属性对象 iot_driver_attribute
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@TableName("iot_driver_attribute")
public class DriverAttribute {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 驱动ID
     */
    private Long driverId;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 属性类型
     */
    private String attributeType;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 必填
     */
    private Boolean required;

    /**
     * 描述
     */
    private String remark;


}
