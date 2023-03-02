package com.pangu.iot.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 点位属性配置信息对象 iot_point_info
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Data
@TableName("iot_point_info")
public class PointInfo implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 点位属性ID
     */
    private Long pointAttributeId;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 设备属性id
     */
    private Long deviceAttributeId;
    /**
     * 值
     */
    private String value;
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
