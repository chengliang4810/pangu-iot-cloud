package org.dromara.manager.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设备属性值
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DeviceAttributeValue implements Serializable {

    /**
     * 设备代码
     */
    private String deviceCode;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 属性值
     */
    private String value;

}
