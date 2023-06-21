package org.dromara.common.emqx.constant;

/**
 * 订阅模式
 *
 * @author chengliang
 * @date 2022/08/01
 */
public enum Pattern {
    /**
     * 普通订阅
     */
    NONE,
    /**
     * 不带群组的共享订阅
     */
    QUEUE,
    /**
     * 带群组的共享订阅
     */
    SHARE;
}
