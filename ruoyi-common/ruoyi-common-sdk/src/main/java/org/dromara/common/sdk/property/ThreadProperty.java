package org.dromara.common.sdk.property;

import lombok.Data;

/**
 * 通用线程池属性
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Data
public class ThreadProperty {
    private String prefix;
    private int corePoolSize;
    private int maximumPoolSize;
    private int keepAliveTime;
}
