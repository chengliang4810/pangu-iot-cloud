package org.dromara.common.iot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 元数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSyncDownDTO implements Serializable {

    /**
     * 待同步数据内容
     */
    private String content;

}
