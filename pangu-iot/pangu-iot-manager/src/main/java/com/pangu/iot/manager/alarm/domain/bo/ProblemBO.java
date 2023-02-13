package com.pangu.iot.manager.alarm.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 告警记录业务对象
 *
 * @author chengliang4810
 * @date 2023-02-13
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProblemBO extends BaseEntity {

    /**
     * 是否需要设备信息
     */
    private Boolean deviceInfo;

    /**
     * event_id
     */
    @NotNull(message = "event_id不能为空", groups = { EditGroup.class })
    private Long eventId;

    /**
     * 对象ID
     */
    @NotNull(message = "对象ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long objectId;

    /**
     * 告警级别
     */
    private Integer severity;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 待确认状态
     */
    @NotNull(message = "待确认状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long acknowledged;

    /**
     * 时间
     */
    @NotNull(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date clock;

    /**
     * 解决时间
     */
    @NotNull(message = "解决时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date rClock;

    /**
     * 设备ID
     */
    @NotNull(message = "设备ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long deviceId;


}
