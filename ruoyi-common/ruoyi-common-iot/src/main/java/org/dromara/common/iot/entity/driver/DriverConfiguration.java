package org.dromara.common.iot.entity.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author pnoker
 * @since 2022.1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String command;
    private Object content;
}
