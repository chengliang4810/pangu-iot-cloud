package org.dromara.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 设备属性对象 iot_device_attribute
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_device_attribute")
public class DeviceAttribute extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 设备编号
     */
    private Long deviceId;

    /**
     * 属性名称
     */
    private String attributeName;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 属性类型
     */
    private String attributeType;

    /**
     * 单位
     */
    private String unit;

    /**
     * 数据预处理代码
     */
    private String pretreatmentScript;

    /**
     * 值映射ID
     */
    private Long valueMapId;

    /**
     * 描述
     */
    private String remark;


}
