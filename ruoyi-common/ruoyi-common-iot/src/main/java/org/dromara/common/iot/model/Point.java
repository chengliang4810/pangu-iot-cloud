package org.dromara.common.iot.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 点位对应物联网的设备属性
 *
 * @author chengliang
 * @date 2023/06/28
 */
@Data
@Accessors(chain = true)
public class Point implements Serializable {

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
     * 数据预处理代码
     */
    private String pretreatmentScript;

}
