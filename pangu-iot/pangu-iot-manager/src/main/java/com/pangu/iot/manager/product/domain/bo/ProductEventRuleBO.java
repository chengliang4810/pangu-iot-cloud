package com.pangu.iot.manager.product.domain.bo;

import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
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
 * 告警规则
 *
 * @author chengliang4810
 * @date 2023/02/03 10:28
 */

@Getter
@Setter
public class ProductEventRuleBO {

    @NotNull(groups = {EditGroup.class, RemoveGroup.class})
    private Long eventRuleId;

    @NotNull(groups = {AddGroup.class, EditGroup.class})
    private Integer eventNotify; // 0 否 1 是，默认 1

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

    @NotBlank(groups = {AddGroup.class, EditGroup.class})
    private String expLogic; // and or

    private String remark;

    private List<DeviceService> deviceServices;

    private List<Tag> tags;

    // @NotNull(groups = EditGroup.class)
    private String zbxId;

    @NotNull(groups = AddGroup.class)
    private Long productId; // 产品ID

    private Integer classify = 0;

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

        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String function; // last avg max min sum change nodata

        private String scope; // s m h T

        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String condition; // > < = <> >= <=

        @NotBlank(groups = {AddGroup.class, EditGroup.class})
        private String value;

        @NotNull(groups = {AddGroup.class, EditGroup.class})
        private String productAttributeKey; // 产品属性 Key

        private String productId; // 产品 ID

        private String unit;

        private Long productAttributeId;

        private String productAttributeType;

        private String attributeValueType;

        private Integer period;

        @Override
        public String toString() {
            StringBuilder expression = new StringBuilder();
            expression.append(function);
            expression.append("(/");
            expression.append(productId);
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

        private String deviceId;

        private String executeDeviceId;

        private Long serviceId;
    }

}




