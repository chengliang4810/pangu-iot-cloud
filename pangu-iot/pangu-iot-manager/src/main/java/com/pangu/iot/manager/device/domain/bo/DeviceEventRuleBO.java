package com.pangu.iot.manager.device.domain.bo;

import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.validate.QueryGroup;
import com.pangu.common.core.validate.RemoveGroup;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class DeviceEventRuleBO {

    @NotNull(groups = {EditGroup.class, RemoveGroup.class, QueryGroup.class})
    private Long eventRuleId;

    /**
     * 事件通知
     * 0 否 1 是，默认 1
     */
    @NotNull(groups = {AddGroup.class, EditGroup.class})
    private Byte eventNotify;

    // 告警规则名称
    @NotBlank(groups = {AddGroup.class, EditGroup.class})
    private String eventRuleName;

    // 告警规则级别
    @NotNull(groups = {AddGroup.class, EditGroup.class})
    private Integer eventLevel;

    // 表达式列表
    @Valid
    @NotEmpty(groups = {AddGroup.class, EditGroup.class})
    private List<Expression> expList;

    /**
     * 逻辑
     * // and or
     */
    @NotBlank(groups = {AddGroup.class, EditGroup.class})
    private String expLogic;

    private String remark;

    private List<DeviceService> deviceServices;

    private List<Tag> tags;

    @NotNull(groups = EditGroup.class)
    private String zbxId;

    @NotBlank(groups = {QueryGroup.class, RemoveGroup.class})
    private Long deviceId;

    @NotBlank(groups = QueryGroup.class)
    private String status;

    private String classify = "0";

    @Data
    public static class Tag {

        @Max(20)
        private String tag;

        @Max(50)
        private String value;
    }

    @Getter
    @Setter
    // 告警表达式 构成
    public static class Expression {

        private Long eventExpId;

        /**
         * 函数
         * last avg max min sum change nodata
         */
        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String function;

        /**
         * 范围
         * s m h T
         */
        private String scope;

        /**
         * 条件
         * > < = <> >= <=
         */
        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String condition;

        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String value;

        /**
         * 产品attr关键
         * 产品属性 Key
         */
        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String productAttributeKey;

        /**
         * 设备id
         */
        private Long deviceId;

        private String unit;

        private Long productAttributeId;

        private Integer productAttributeType;

        private String attrValueType;

        private Integer period;

        @Override
        public String toString() {
            StringBuilder expression = new StringBuilder();
            expression.append(function);
            expression.append("(/");
            expression.append(deviceId);
            expression.append("/");
            expression.append(productAttributeKey);

            if (StringUtils.isNotBlank(scope)) {
                expression.append(", ");
                expression.append(scope);
            }
            expression.append(") ").append(condition).append(" ").append(StringUtils.addQuotes(value));
            return expression.toString();
        }
    }


    @Setter
    @Getter
    public static class DeviceService {

        private Long deviceId;

        private Long executeDeviceId;

        private Long serviceId;
    }

}




