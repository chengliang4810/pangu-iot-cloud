package com.pangu.common.emqx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * emq属性
 *
 * @author chengliang
 * @date 2022/08/02
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt.config")
public class EmqProperties {

    /**
     * emq服务器地址
     */
    private String broker;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
     */
    private Boolean cleanSession;
    /**
     * 是否断线重连
     */
    private Boolean reconnect;
    /**
     * 连接超时时间
     */
    private Integer timeout;
    /**
     * 心跳间隔
     */
    private Integer keepAlive;

}
