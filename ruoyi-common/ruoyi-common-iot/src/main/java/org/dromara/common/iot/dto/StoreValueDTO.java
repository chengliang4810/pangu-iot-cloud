package org.dromara.common.iot.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * td引擎值对象
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Data
@Accessors(chain = true)
public class StoreValueDTO {

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 采集时间
     */
    private Long originTime;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 值
     */
    private String value;

}
