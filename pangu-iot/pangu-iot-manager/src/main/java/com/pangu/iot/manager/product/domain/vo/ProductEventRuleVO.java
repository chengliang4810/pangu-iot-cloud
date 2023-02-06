package com.pangu.iot.manager.product.domain.vo;

import com.pangu.common.core.utils.StringUtils;
import com.pangu.iot.manager.product.domain.ProductEventExpression;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 告警规则
 *
 * @author chengliang4810
 * @date 2023/02/03 10:28
 */

@Getter
@Setter
public class ProductEventRuleVO {

    private Long eventRuleId;

    private Integer eventNotify; // 0 否 1 是，默认 1

    // 告警规则名称
    private String eventRuleName;

    // 告警规则级别
    private Integer eventLevel;

    // 表达式列表
    private List<ProductEventExpression> expList;

    private String expLogic; // and or

    private String remark;

    private List<DeviceService> deviceServices;

    private Long productId; // 产品ID

    private Boolean status;

    private Integer classify = 0;

    @Data
    public static class Tag {

        private String tag;

        private String value;
    }

    @Getter
    @Setter
    // 告警表达式 构成
    public static class Expression {

        private Long eventExpId;

        private String function; // last avg max min sum change nodata

        private String scope; // s m h T

        private String condition; // > < = <> >= <=

        private String value;

        private String productAttrKey; // 产品属性 Key

        private String productId; // 产品 ID

        private String unit;

        private Long productAttrId;

        private String productAttrType;

        private String attrValueType;

        private Integer period;

        @Override
        public String toString() {
            StringBuilder expression = new StringBuilder();
            expression.append(function);
            expression.append("(/");
            expression.append(productId);
            expression.append("/");
            expression.append(productAttrKey);

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




