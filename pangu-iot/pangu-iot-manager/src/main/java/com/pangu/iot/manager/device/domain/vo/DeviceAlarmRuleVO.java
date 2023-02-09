package com.pangu.iot.manager.device.domain.vo;

import com.pangu.common.core.validate.QueryGroup;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 设备事件规则
 * 告警规则
 *
 * @author chengliang
 * @date 2023/02/08
 */

@Getter
@Setter
public class DeviceAlarmRuleVO {

    private Long eventRuleId;

    /**
     * 事件通知
     * 0 否 1 是，默认 1
     */
    private Byte eventNotify;

    // 告警规则名称
    private String eventRuleName;

    // 告警规则级别
    private Integer eventLevel;

    // 表达式列表
    private List<ProductEventExpression> expList;

    /**
     * 继承
     */
    private Boolean inherit;
    /**
     * 逻辑
     * // and or
     */
    private String expLogic;

    private String remark;

    private List<DeviceService> deviceServices;

    private String zbxId;

    private Long deviceId;

    @NotBlank(groups = QueryGroup.class)
    private String status;

    private String classify = "0";


    @Setter
    @Getter
    public static class DeviceService {

        private Long deviceId;

        private Long executeDeviceId;

        private Long serviceId;

    }

}




