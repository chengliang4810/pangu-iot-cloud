package com.pangu.iot.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 协议驱动对象 iot_driver
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@TableName("iot_driver")
public class Driver implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 协议名称
     */
    private String name;
    /**
     * 显示名称
     */
    private String displayName;
    /**
     * 协议服务名称
     */
    private String serviceName;
    /**
     * 主机IP
     */
    private String host;
    /**
     * 端口
     */
    private Long port;
    /**
     * 启用|禁用
     */
    private Long enable;
    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
