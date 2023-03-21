package com.pangu.iot.manager.device.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 功能执行记录对象 iot_service_execute_record
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("iot_service_execute_record")
public class ServiceExecuteRecord extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 功能名称
     */
    private String serviceName;
    /**
     * 参数
     */
    private String param;
    /**
     * 设备ID
     */
    private Long deviceId;
    /**
     * 执行方式   手动触发  场景触发
     */
    private Integer executeType;
    /**
     * 执行人执行方式未手动触发时有值
     */
    private String executeUser;
    /**
     * 执行场景ID
     */
    private Long executeRuleId;

    private Boolean executeStatus;


    public ServiceExecuteRecord(Long id, Boolean executeStatus) {
        this.id = id;
        this.executeStatus = executeStatus;
    }


}
