package com.pangu.common.emqx.encoder;

/**
 * 消息编码器
 *
 * @author chengliang
 * @date 2022/08/01
 */
public interface MessageEncoder<T> {

    /**
     * 消息编码器
     * @param entity
     * @return
     */
    String encoder(T entity);

}

