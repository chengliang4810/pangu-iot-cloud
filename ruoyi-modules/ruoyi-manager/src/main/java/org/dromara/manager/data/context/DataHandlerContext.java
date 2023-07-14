package org.dromara.manager.data.context;

import lombok.Data;

/**
 * 数据处理上下文
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Data
public class DataHandlerContext {

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备代码
     */
    private String deviceCode;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 属性类型
     */
    private String attributeType;

    /**
     * 属性预处理脚本
     */
    private String attributeScript;

    /**
     * 采集时间
     */
    private Long originTime;

    /**
     * 原始值
     */
    private String originalValue;

    /**
     * 值，经过每个流程value都可能变化
     */
    private String value;

}
