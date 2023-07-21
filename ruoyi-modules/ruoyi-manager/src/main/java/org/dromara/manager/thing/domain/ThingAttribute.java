package org.dromara.manager.thing.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 物模型属性对象
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_thing_attribute")
public class ThingAttribute extends BaseEntity {

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
